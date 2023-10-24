package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.Cache;
import ru.otus.cache.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);
    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final Cache<String, Client> cache;
    private boolean useCache;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.cache = new MyCache<>();
        this.useCache = false;
    }

    public void switchCacheUsing() {
        useCache = !useCache;
        log.info("cache status changed to: {}", useCache);
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                var clientSaved = clientDataTemplate.insert(session, clientCloned);
                putInCache(clientSaved);
                return clientSaved;
            }
            var clientSaved = clientDataTemplate.update(session, clientCloned);
            putInCache(clientSaved);
            return clientSaved;
        });
    }

    private void putInCache(Client client) {
        cache.put(client.getId().toString(), client);
    }

    @Override
    public Optional<Client> getClient(long id) {
        if (useCache) {
            if (cache.keyPresent(String.valueOf(id))) {
                return getFromCache(id);
            }
            Optional<Client> clientOptional = getFromDb(id);
            clientOptional.ifPresent(this::putInCache);
            return clientOptional;
        }
        return getFromDb(id);
    }

    private Optional<Client> getFromDb(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            return clientDataTemplate.findById(session, id);
        });
    }

    private Optional<Client> getFromCache(long id) {
        return Optional.ofNullable(cache.get(String.valueOf(id)));
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(clientDataTemplate::findAll);
    }

}

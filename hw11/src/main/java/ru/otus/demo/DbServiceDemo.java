package ru.otus.demo;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.List;

public class DbServiceDemo {
    private static final int CLIENT_QTY = 300;
    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
///
        log.info("inserting {} clients...", CLIENT_QTY);
        for (int i = 0; i < CLIENT_QTY; i++) {
            dbServiceClient.saveClient(new Client(null, "client" + i, new Address(null, "street" + i),
                    List.of(new Phone(null, "123-" + StringUtils.leftPad(String.valueOf(i), 4, '0')))));
        }
        log.info("finding all clients...");
        long startTime1 = System.currentTimeMillis();
        dbServiceClient.findAll();
        long finishTime1 = System.currentTimeMillis();

        dbServiceClient.switchCacheUsing();

        long startTime2 = System.currentTimeMillis();
        dbServiceClient.findAll();
        long finishTime2 = System.currentTimeMillis();
        log.info("find {} clients with cache elapsed {} ms)", CLIENT_QTY, finishTime2 - startTime2);
        log.info("... without cache elapsed {} ms)", finishTime1 - startTime1);
    }
}

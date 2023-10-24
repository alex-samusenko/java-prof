package ru.otus.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements Cache<K, V> {
    private static final Logger log = LoggerFactory.getLogger(MyCache.class);
    private final Map<K, V> cache;
    private final List<Listener<K,V>> listeners;

    public MyCache() {
        this.cache = new WeakHashMap<>();
        this.listeners = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        log.debug("cache size after put: {}", cache.size());
        listeners.forEach(listener -> listener.notify(key, value, "put"));
    }

    @Override
    public void remove(K key) {
        V remove = cache.remove(key);
        log.debug("cache size after remove: {}", cache.size());
        listeners.forEach(listener -> listener.notify(key, remove, "remove"));
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        log.debug("cache size after get: {} a value is: {}", cache.size(), value);
        listeners.forEach(listener -> listener.notify(key, value, "get"));
        return value;
    }

    @Override
    public void addListener(Listener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener<K, V> listener) {
            listeners.remove(listener);
    }

    @Override
    public boolean keyPresent(K key) {
        return cache.containsKey(key);
    }
}

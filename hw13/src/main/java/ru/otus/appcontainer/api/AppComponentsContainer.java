package ru.otus.appcontainer.api;

public interface AppComponentsContainer {
    <T> T getAppComponent(Class<T> componentClass);
    <T> T getAppComponent(String componentName);
}

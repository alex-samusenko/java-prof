package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static java.util.Optional.ofNullable;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) throws Exception {
        for(Class<?> initialConfigClass: initialConfigClasses) checkConfigClass(initialConfigClass);
        List<Class<?>> configClasses = Arrays.stream(initialConfigClasses)
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponentsContainerConfig.class)
                        .order())).toList();
        for (Class<?> configClass : configClasses) {
            processConfig(configClass);
        }
    }

    private void processConfig(Class<?> configClass) throws Exception {
        checkConfigClass(configClass);
        Object configClassInstance = configClass.getConstructor().newInstance();
        List<Method> methods = Arrays.stream(configClass.getMethods()).filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order())).toList();
        String componentName;
        Object component;
        for (Method method : methods) {
            componentName = getComponentName(method);
            checkForDuplicates(componentName);
            component = method.invoke(configClassInstance, getArgs(method.getParameters()));
            appComponentsByName.put(componentName, component);
            appComponents.add(component);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <T> T getAppComponent(Class<T> componentClass) {
        List<Object> components = appComponents.stream().filter(component -> componentClass.isAssignableFrom(component.getClass())).toList();
        if (components.size() > 1) {
            throw new AppContainerException("getting component is present in container more then one instance");
        } else if (components.isEmpty()) {
            throw new AppContainerException("getting component is absent in container");
        }
        return (T) components.get(0);
    }

    @Override
    public <T> T getAppComponent(String componentName) {
        return (T) ofNullable(appComponentsByName.get(componentName))
                .orElseThrow(() -> new AppContainerException("Component is not in container"));
    }
    private String getComponentName(Method method) {
        return method.getAnnotation(AppComponent.class).name();
    }

    private void checkForDuplicates(String name) {
        if(appComponentsByName.containsKey(name)){
            throw new AppContainerException("Duplicate:" + name);
        }
    }

    private Object[] getArgs(Parameter[] parameters) {
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < args.length; i++) {
            args[i] = getAppComponent(parameters[i].getType());
        }
        return args;
    }
}

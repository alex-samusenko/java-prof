package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

class Ioc {

    private Ioc() {
    }

    @SuppressWarnings("unchecked")
    static <T> T createMyClass(Class<T> interfaceClass, T interfaceInstance) {
        InvocationHandler handler = new DemoInvocationHandler(interfaceInstance);
        return (T) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{interfaceClass}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final Object loggingInterface;
        private final List<String> LogMethods;
        private final Map<String, Object> cache = new HashMap<>();

        DemoInvocationHandler(Object loggingInterface) {
            this.loggingInterface = loggingInterface;

            List<String> list = new ArrayList<>();
            for (Method method : loggingInterface.getClass().getMethods()) {
                if (Arrays.stream(method.getAnnotations())
                        .anyMatch(Log.class::isInstance)) {
                    list.add(getMethodHeader(method));
                }
            }
            LogMethods = list;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            String[] paramValues = Arrays.stream(args).map(Object::toString).toArray(String[]::new);
            String cacheKey = String.format("%s , param(s): %s", method.getName(), String.join(", ", paramValues));
            if (LogMethods.contains(getMethodHeader(method)))
                System.out.println("executed method: " + cacheKey);
            if (cache.containsKey(cacheKey)) {
                return cache.get(cacheKey);
            }
            else {
                Object result = method.invoke(loggingInterface, args);
                cache.put(cacheKey, result);
                return result;
            }
        }

        private String getMethodHeader(Method method) {
            String[] paramTypes = Arrays.stream(method.getParameters())
                    .map(Object::toString).toArray(String[]::new);
            return String.format("%s %s", method.getName(), String.join(", ", paramTypes));
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + loggingInterface +
                    '}';
        }
    }
}

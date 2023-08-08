package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Ioc {

    private Ioc() {
    }

    static <T> T createMyClass(Class<T> interfaceClass, T interfaceInstance) {
        InvocationHandler handler = new DemoInvocationHandler(interfaceInstance);
        return (T) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{interfaceClass}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final Object loggingInterface;

        private final Map<String, Object> cache = new HashMap<>();

        DemoInvocationHandler(Object loggingInterface) {
            this.loggingInterface = loggingInterface;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            String[] params = Arrays.stream(args).map(Object::toString).toArray(String[]::new);

            if (loggingInterface.getClass().getMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Log.class)) {
                System.out.println("executed method: " + method.getName() + ", params: " + String.join(",", params));
            }

            String cacheKey = loggingInterface.getClass().toString() + '.' + method.getName() + "(" + String.join(",", params) + ")";

            if (cache.containsKey(cacheKey)) {
                return cache.get(cacheKey);
            }
            else {
                Object result = method.invoke(loggingInterface, args);
                cache.put(cacheKey, result);
                return result;
            }
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + loggingInterface +
                    '}';
        }
    }
}

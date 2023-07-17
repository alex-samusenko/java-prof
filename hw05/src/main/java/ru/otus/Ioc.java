package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

class Ioc {

    private Ioc() {
    }

    static LoggingInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new LoggingImpl());
        return (LoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{LoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final LoggingInterface loggingInterface;

        DemoInvocationHandler(LoggingInterface myClass) {
            this.loggingInterface = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (loggingInterface.getClass().getMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Log.class)) {
                String[] params = Arrays.stream(args).map(Object::toString).toArray(String[]::new);
                System.out.println("executed method: " + method.getName() + ", params: " + String.join(",", params));
            }
            return method.invoke(loggingInterface, args);
        }

/*        static void printMethods(Object obj) {
            for (Method m : obj.getClass().getDeclaredMethods()){
                System.out.println(m.toString() + " has Log annotation: " + m.isAnnotationPresent(Log.class));
            }
        }*/

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + loggingInterface +
                    '}';
        }
    }
}

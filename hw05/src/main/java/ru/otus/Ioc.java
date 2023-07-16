package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
            //Method m = loggingInterface.getClass().getMethod(method.getName());
            //printMethods(loggingInterface);
            if (loggingInterface.getClass().getMethod(method.getName(), int.class).isAnnotationPresent(Log.class)) {
                System.out.println("executed method: " + method.getName() + ", param: " + args[0].toString());
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

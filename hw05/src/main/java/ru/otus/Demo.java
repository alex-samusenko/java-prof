package ru.otus;

public class Demo {
    public static void main(String[] args) {
        LoggingInterface myClass = Ioc.createMyClass(LoggingInterface.class, new LoggingImpl());
        myClass.calculation(6);
    }
}




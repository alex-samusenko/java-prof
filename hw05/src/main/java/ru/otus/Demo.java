package ru.otus;

public class Demo {
    public static void main(String[] args) {
        LoggingInterface myClass = Ioc.createMyClass();
        myClass.calculation(6);
    }
}




package ru.otus;

public class App {
    public static void main(String[] strings) {
        Object lock = new Object();
        new CustomThread(lock).start();
        new CustomThread(lock).start();
    }
}

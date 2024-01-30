package ru.otus;

public class Main {
    public static void main(String[] strings) {
        Object lock = new Object();
        new CustomThread(lock, "first").start();
        new CustomThread(lock, "second").start();
    }
}

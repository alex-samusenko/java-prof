package ru.otus;


public class LoggingImpl implements LoggingInterface {

    @Override
    @Log
    public void calculation(int param) {}

    @Override
    public String toString() {
        return "LoggingImpl{}";
    }
}

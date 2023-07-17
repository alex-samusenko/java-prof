package ru.otus;


public class LoggingImpl implements LoggingInterface {

    @Override
    @Log
    public void calculation(int param1, int param2) {}

    @Override
    public String toString() {
        return "LoggingImpl{}";
    }
}

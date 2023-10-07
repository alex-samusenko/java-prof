package ru.otus;


public class LoggingImpl implements LoggingInterface {
    @Log
    public void calculation(int param1) {}

    @Override
    @Log
    public void calculation(int param1, int param2) {}

    @Override
    @Log
    public void calculation(int param1, int param2, int param3) {}

    @Override
    public String toString() {
        return "LoggingImpl{}";
    }
}

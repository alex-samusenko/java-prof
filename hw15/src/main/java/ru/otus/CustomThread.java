package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(CustomThread.class);
    private final Object lock;
    private int counter;
    private Boolean direction;

    public CustomThread(Object object) {
        this.lock = object;
        this.counter = 1;
        this.direction = false;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (lock) {
                try {
                    if (counter == 1 || counter == 10) direction = !direction;
                    logger.info(String.valueOf(direction ? counter++ : counter--));
                    sleep();
                    lock.notify();
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}

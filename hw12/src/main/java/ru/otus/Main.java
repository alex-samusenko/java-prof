package ru.otus;

import ru.otus.config.*;

public class Main {
    public static void main(String[] args) throws Exception {
        var dbServiceClient = new HibernateConfigImpl().configure();
        var clientWebServer = new WebServerConfigImpl(dbServiceClient).configure();

        clientWebServer.start();
        clientWebServer.join();
    }
}

package ru.otus.config;

import ru.otus.server.ClientWebServer;

import java.io.IOException;

public interface WebServerConfig {
    ClientWebServer configure() throws IOException;
}

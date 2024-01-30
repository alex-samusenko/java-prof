package ru.otus.config;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.helper.ClientHelper;
import ru.otus.helper.FileSystemHelper;
import ru.otus.server.ClientWebServer;
import ru.otus.server.ClientWebServerImpl;
import ru.otus.service.TemplateProcessor;
import ru.otus.service.TemplateProcessorImpl;

import java.io.IOException;

public class WebServerConfigImpl implements WebServerConfig {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";
    private final DBServiceClient dbServiceClient;

    public WebServerConfigImpl(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public ClientWebServer configure() {
        String configPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, configPath);
        var clientHelper = new ClientHelper();
        var templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        return new ClientWebServerImpl(WEB_SERVER_PORT, loginService, dbServiceClient, clientHelper, templateProcessor);
    }
}

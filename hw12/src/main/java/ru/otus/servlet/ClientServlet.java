package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.helper.ClientHelper;
import ru.otus.service.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientServlet extends HttpServlet {
    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";

    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;
    private final ClientHelper clientHelper;

    public ClientServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient, ClientHelper clientHelper) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
        this.clientHelper = clientHelper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_ATTR_CLIENTS, dbServiceClient.findAll()
                .stream()
                .map(clientHelper::mapToTemplateData)
                .toList());

        response.setContentType("text/html");
        try (var writer = response.getWriter()) {
            writer.println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        dbServiceClient.saveClient(clientHelper.mapToClient(req));
        response.sendRedirect(req.getServletPath());
    }
}

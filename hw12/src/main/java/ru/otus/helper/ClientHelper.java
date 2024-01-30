package ru.otus.helper;

import jakarta.servlet.http.HttpServletRequest;
import ru.otus.crm.model.*;

import java.util.Collections;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class ClientHelper {
    public Map<String, Object> mapToTemplateData(Client client) {
        if (client == null) {
            return Collections.emptyMap();
        } else {
            return Map.of("id", client.getId(),
                    "name", client.getName(),
                    "address", client.getAddress() != null ? client.getAddress().getStreet() : "",
                    "phones", client.getPhones() == null ? "" :
                            client.getPhones()
                                    .stream()
                                    .map(Phone::getNumber)
                                    .collect(joining(",")));
        }
    }

    public Client mapToClient(HttpServletRequest request) {
        var client = new Client();
        client.setName(request.getParameter("name"));
        var address = request.getParameter("address");
        client.setAddress(address != null ? new Address(address) : null);
        var phones = request.getParameter("phone");
        if (phones != null) {
            client.setPhones(
                    stream(phones.split(","))
                            .map(phone -> new Phone(phone, client))
                            .toList());
        }
        return client;
    }
}

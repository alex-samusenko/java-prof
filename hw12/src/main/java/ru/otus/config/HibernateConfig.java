package ru.otus.config;

import ru.otus.crm.service.DBServiceClient;

public interface HibernateConfig {
    DBServiceClient configure();
}

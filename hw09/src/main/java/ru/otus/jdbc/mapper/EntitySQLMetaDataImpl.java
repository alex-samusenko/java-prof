package ru.otus.jdbc.mapper;

import java.util.stream.Collectors;

import java.lang.reflect.Field;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> entityClassMetaData;
    private final String allFieldsStr;
    private final String fieldsWithoutIdStr;
    private final String fieldsWithoutIdPlaceholderStr;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        this.allFieldsStr = entityClassMetaData.getAllFields()
                .stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
        this.fieldsWithoutIdStr = entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
        this.fieldsWithoutIdPlaceholderStr = entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(s -> "?")
                .collect(Collectors.joining(","));
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select %s from %s", allFieldsStr,
                entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("select %s from %s where %s = ?", allFieldsStr,
                entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        return String.format("insert into %s(%s) values(%s)",
                entityClassMetaData.getName(), fieldsWithoutIdStr, fieldsWithoutIdPlaceholderStr);
    }

    @Override
    public String getUpdateSql() {
        return String.format("update %s set (%s) = (%s) where %s = ?",
                entityClassMetaData.getName(), fieldsWithoutIdStr, fieldsWithoutIdPlaceholderStr,
                entityClassMetaData.getIdField().getName());
    }
}

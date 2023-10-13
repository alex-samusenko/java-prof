package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/*
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createEntity(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var entityList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    entityList.add(createEntity(rs));
                }
                return entityList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("dbExecutor error"));
    }

    @Override
    public long insert(Connection connection, T entity) {
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getFieldParamsWithoutId(entity));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T entity) {
        try {
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), addIdFieldParam(getFieldParamsWithoutId(entity), entity));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
    private T createEntity(ResultSet rs) throws SQLException {
        T entity;
        try {
            Object[] args = new Object[entityClassMetaData.getAllFields().size()];
            for (int i = 0; i < args.length; i++) {
                args[i] = rs.getObject(entityClassMetaData.getAllFields().get(i).getName());
            }
            entity = entityClassMetaData.getConstructor().newInstance(args);
        }catch (InvocationTargetException | InstantiationException | IllegalAccessException e){
            throw new RuntimeException(e.getMessage());
        }
        return entity;
    }

    private List<Object> getFieldParamsWithoutId(T entity) throws IllegalAccessException {
        List<Object> list = new ArrayList<>();
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            list.add(field.get(entity));
            field.setAccessible(false);
        }
        return list;
    }

    private List<Object> addIdFieldParam(List<Object> list, T entity) throws IllegalAccessException {
        Field idField = entityClassMetaData.getIdField();
        idField.setAccessible(true);
        Object result = idField.get(entity);
        idField.setAccessible(false);
        list.add(result);
        return list;
    }

}

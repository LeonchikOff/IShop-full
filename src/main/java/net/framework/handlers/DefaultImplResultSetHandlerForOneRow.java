package net.framework.handlers;

import net.framework.annotations.jdbc.mapping.JoinTable;
import net.framework.annotations.jdbc.mapping.Column;
import net.framework.converters.Converter;
import net.framework.converters.DefaultConverter;
import net.framework.exception.FrameworkSystemException;
import net.framework.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultImplResultSetHandlerForOneRow<T> implements ResultSetHandler<T> {
    protected final Class<T> targetEntityClass;
    protected final Converter converter;

    public DefaultImplResultSetHandlerForOneRow(Class<T> targetEntityClass, Converter converter) {
        this.targetEntityClass = targetEntityClass;
        this.converter = converter;
    }

    public DefaultImplResultSetHandlerForOneRow(Class<T> targetEntityClass) {
        this(targetEntityClass, new DefaultConverter());
    }

    @Override
    public T handle(ResultSet resultSet) throws SQLException {
        try {
            T instanceOfTargetEntity = targetEntityClass.newInstance();
            List<Field> fieldsOfTargetEntity = ReflectionUtils.getAccessibleFieldsOfEntity(targetEntityClass);
            List<String> listNameOfColumnsFromDB = getNameOfColumnsFromResultSet(resultSet);
            populateFieldsTargetEntity(instanceOfTargetEntity, fieldsOfTargetEntity, listNameOfColumnsFromDB, resultSet);
            return instanceOfTargetEntity;
        } catch (InstantiationException e) {
            throw new FrameworkSystemException("Accessible constructor without parameters was not found or class is abstract", e);
        } catch (IllegalAccessException e) {
            throw new FrameworkSystemException(e);
        }
    }

    protected void populateFieldsTargetEntity(Object instanceOfTargetEntity, List<Field> fieldsOfTargetEntity, List<String> listNameOfColumnsFromDB, ResultSet resultSet) throws IllegalAccessException, InstantiationException, SQLException {
        for (Field field : fieldsOfTargetEntity) {
            Class<?> fieldType = field.getType();
            JoinTable joinTableAnnotation = field.getAnnotation(JoinTable.class);
            if (joinTableAnnotation == null) {
                Column fieldColumnAnnotation = field.getAnnotation(Column.class);
                String nameOfFieldEqualColumnDB = fieldColumnAnnotation == null ? field.getName() : fieldColumnAnnotation.columnName();
                if (listNameOfColumnsFromDB.contains(nameOfFieldEqualColumnDB)) {
                    Object valueOfFieldFromBD = resultSet.getObject(nameOfFieldEqualColumnDB);
                    field.set(instanceOfTargetEntity, converter.convert(fieldType, valueOfFieldFromBD));
                }
            } else {
                Object instanceOfEmbeddedEntity = fieldType.newInstance();
                List<Field> fieldsOfEmbeddedEntity = ReflectionUtils.getAccessibleFieldsOfEntity(fieldType);
                populateFieldsTargetEntity(instanceOfEmbeddedEntity, fieldsOfEmbeddedEntity, listNameOfColumnsFromDB, resultSet);
                field.set(instanceOfTargetEntity, instanceOfEmbeddedEntity);

                Field fieldInEmbeddedEntityToBeJoined = null;
                for (Field fieldEmbeddedEntity : fieldsOfEmbeddedEntity) {
                    if (fieldEmbeddedEntity.getName().equals(joinTableAnnotation.nameOfFieldBeJoined())) {
                        fieldInEmbeddedEntityToBeJoined = fieldEmbeddedEntity;
                    }
                }
                if (fieldInEmbeddedEntityToBeJoined == null) {
                    throw new FrameworkSystemException("Field " + joinTableAnnotation.columnName() + " was not found in the class " + instanceOfEmbeddedEntity);
                } else {
                    fieldInEmbeddedEntityToBeJoined.set(instanceOfEmbeddedEntity, resultSet.getObject(joinTableAnnotation.nameOfFieldBeJoined()));
                }
            }
        }
    }

    protected List<String> getNameOfColumnsFromResultSet(ResultSet resultSet) throws SQLException {
        List<String> nameOfColumns = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            nameOfColumns.add(metaData.getColumnName(i + 1));
        }
        return nameOfColumns;
    }


}

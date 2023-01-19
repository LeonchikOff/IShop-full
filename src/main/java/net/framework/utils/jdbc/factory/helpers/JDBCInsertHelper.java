package net.framework.utils.jdbc.factory.helpers;

import net.framework.annotations.jdbc.Insert;
import net.framework.annotations.jdbc.mapping.JoinTable;
import net.framework.annotations.jdbc.mapping.Column;
import net.framework.annotations.jdbc.mapping.Table;
import net.framework.exception.FrameworkSystemException;
import net.framework.handlers.InsertQuery;
import net.framework.handlers.ResultSetHandler;
import net.framework.utils.JDBCUtils;
import net.framework.utils.ReflectionUtils;
import net.framework.utils.jdbc.JDBCConnectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JDBCInsertHelper extends JDBCAbstractSQLHelper {
    public Object insertDataToDB(Insert annotationInsert, Method method, Object[] argsOfCurrentMethod)
            throws IllegalAccessException, InvocationTargetException {

        if (argsOfCurrentMethod.length != 1) throw new FrameworkSystemException(
                "Method with annotation @Insert: " + method + " should have only one argument.");

        Object entityForInsertToDB = argsOfCurrentMethod[0];
        Class<?> classOfEntity = entityForInsertToDB.getClass();
        Table annotationTable = classOfEntity.getAnnotation(Table.class);
        if (annotationTable == null)
            throw new FrameworkSystemException("Entity class: " + classOfEntity + " does not have annotation @Table.");
        if (annotationTable.generationNextIdExpresion().isEmpty())
            throw new FrameworkSystemException("Annotation @Table for entity class: " + classOfEntity +
                    " should contain nextGenerationIdExpresion parameter.");

        List<Field> fieldsOfInsertEntity = ReflectionUtils.getAccessibleFieldsOfEntity(classOfEntity);

        StringBuilder stringBuilderSQL = new StringBuilder("INSERT INTO ").append(annotationTable.nameOfTable()).append("(");
        StringBuilder valuesInsert = new StringBuilder("values (");
        Field fieldId = null;
        List<Object> params = new ArrayList<>();
        for (Field field : fieldsOfInsertEntity) {
            JoinTable annotationJoinTable = field.getAnnotation(JoinTable.class);
            if (field.getName().equals(annotationTable.nameOfFieldWithId())) {
                valuesInsert.append(annotationTable.generationNextIdExpresion()).append(",");
                fieldId = field;
            } else {
                valuesInsert.append("?,");
                Object fieldOfEntityValue = field.get(entityForInsertToDB);
                if (annotationJoinTable != null) {
                    List<Field> fieldsOfEntityValue = ReflectionUtils.getAccessibleFieldsOfEntity(fieldOfEntityValue.getClass());
                    Field idField = ReflectionUtils.findFieldByName(fieldsOfInsertEntity.getClass(), fieldsOfEntityValue, annotationJoinTable.nameOfFieldBeJoined());
                    fieldOfEntityValue = idField.get(fieldOfEntityValue);
                }
                params.add(fieldOfEntityValue);
            }
            String columnName;
            if (annotationJoinTable != null) {
                columnName = annotationJoinTable.columnName();
            } else {
                Column annotationColumn = field.getAnnotation(Column.class);
                if (annotationColumn != null) {
                    columnName = annotationColumn.columnName();
                } else {
                    columnName = field.getName();
                }
            }
            stringBuilderSQL.append(columnName).append(',');
        }
        valuesInsert.deleteCharAt(valuesInsert.length() - 1);
        stringBuilderSQL
                .deleteCharAt(stringBuilderSQL.length() - 1)
                .append(')')
                .append(valuesInsert)
                .append(')');

        InsertQuery insertQuery = new InsertQuery(fieldId, stringBuilderSQL, params);
        LOGGER.debug("INSERT: {}, {}", insertQuery.getStringBuilderSQL(), insertQuery.getParams());
        ResultSetHandler<?> resultSetHandler =
                createResultSetHandler(annotationInsert.resultSetHandlerClass(), method, classOfEntity);
        Object insertEntity =
                JDBCUtils.insertDataToDB(
                        JDBCConnectionUtils.getCurrentConnection(),
                        insertQuery.getStringBuilderSQL().toString(),
                        resultSetHandler,
                        params.toArray());
        if(fieldId == null) {
            throw new FrameworkSystemException("Id field with name = " + insertQuery.getIdField() + " not found class: " + entityForInsertToDB.getClass());
        }
        Object idValue = fieldId.get(insertEntity);
        fieldId.set(entityForInsertToDB, idValue);
        return null;
    }
}

package net.framework.utils.jdbc.factory.helpers;

import net.framework.annotations.jdbc.Select;
import net.framework.annotations.jdbc.mapping.CollectionItem;
import net.framework.exception.FrameworkSystemException;
import net.framework.handlers.ResultSetHandler;
import net.framework.handlers.SQLBuilder;
import net.framework.utils.JDBCUtils;
import net.framework.utils.jdbc.JDBCConnectionUtils;
import net.framework.handlers.SearchQuery;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public class JDBCSelectHelper extends JDBCAbstractSQLHelper {


    public Object getDataOnSelect(Select annotationSelect, Method currentMethod, Object[] argsOfCurrentMethod) throws InvocationTargetException, IllegalAccessException {
        Class<?> returnTypeOfMethodForResultEntity = this.getReturnTypeOfMethodForResultEntity(currentMethod);
        ResultSetHandler<?> resultSetHandler =
                this.createResultSetHandler(annotationSelect.resultSetHandlerClass(), currentMethod, returnTypeOfMethodForResultEntity);
        Class<? extends SQLBuilder> sqlBuilderClass = annotationSelect.sqlBuilderQueryClass();
        if (sqlBuilderClass == SQLBuilder.class) {
            return JDBCUtils.getDataOnSelect(JDBCConnectionUtils.getCurrentConnection(), annotationSelect.sqlQuery(), resultSetHandler, argsOfCurrentMethod);
        } else {
            return getDataOnSelectThroughCustomSQLBuilder(sqlBuilderClass, resultSetHandler, argsOfCurrentMethod);
        }
    }

    private Class<?> getReturnTypeOfMethodForResultEntity(Method currentMethod) {
        CollectionItem annotationCollectionItem = currentMethod.getAnnotation(CollectionItem.class);
        if (annotationCollectionItem != null) {
            return annotationCollectionItem.parameterizedClass();
        } else {
            Class<?> methodReturnType = currentMethod.getReturnType();
            if (methodReturnType.isArray()) {
                throw new FrameworkSystemException("Use collections instead of array for method: " + currentMethod);
            } else if (Collection.class.isAssignableFrom(methodReturnType)) {
                throw new FrameworkSystemException("Use annotation @CollectionItem to specify type of collection item for method: " + currentMethod);
            } else {
                return methodReturnType;
            }
        }
    }

    private Object getDataOnSelectThroughCustomSQLBuilder(Class<? extends SQLBuilder> sqlBuilderClass, ResultSetHandler<?> resultSetHandler, Object[] args) throws IllegalAccessException {
        try {
            SQLBuilder sqlBuilderInstants = sqlBuilderClass.newInstance();
            SearchQuery searchQuery = sqlBuilderInstants.buildSearchQuery(args);
            LOGGER.debug("Custom SELECT: {}, {}", searchQuery.getStringBuilderSQL(), searchQuery.getParams());
            return JDBCUtils.getDataOnSelect(
                    JDBCConnectionUtils.getCurrentConnection(), searchQuery.getStringBuilderSQL().toString(),
                    resultSetHandler, searchQuery.getParams().toArray());
        } catch (InstantiationException e) {
            throw new FrameworkSystemException("Can't create instance of SQLBuilder for class: " + sqlBuilderClass, e);
        }
    }
}

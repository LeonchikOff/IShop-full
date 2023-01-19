package net.framework.utils.jdbc.factory.helpers;

import net.framework.exception.FrameworkSystemException;
import net.framework.handlers.DefaultImplResultSetHandlerForOneRow;
import net.framework.handlers.IntegerResultSetHandler;
import net.framework.handlers.ResultSetHandler;
import net.framework.handlers.customized.DefaultListResultSetHandler;
import net.framework.handlers.customized.DefaultUniqueResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;


abstract class JDBCAbstractSQLHelper {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected ResultSetHandler<?> createResultSetHandler(Class<? extends ResultSetHandler> resultSetHandlerClass,
                                                         Method method, Class<?> classForTargetEntity) throws InvocationTargetException, IllegalAccessException {
        try {
//            if (classForTargetEntity == Integer.TYPE) {
            if (method.getReturnType() == Integer.TYPE) {
                return new IntegerResultSetHandler();
            } else {
                ResultSetHandler<?> resultSetHandler;
                if (DefaultImplResultSetHandlerForOneRow.class.isAssignableFrom(resultSetHandlerClass)) {
                    resultSetHandler = resultSetHandlerClass.getConstructor(Class.class).newInstance(classForTargetEntity);
                } else {
                    resultSetHandler = resultSetHandlerClass.newInstance();
                }
                if (Collection.class.isAssignableFrom(method.getReturnType())) {
                    return new DefaultListResultSetHandler<>(resultSetHandler);
                } else {
                    return new DefaultUniqueResultSetHandler<>(resultSetHandler);
                }

            }
        } catch (InstantiationException | NoSuchMethodException e) {
            throw new FrameworkSystemException("Can't create instance of ResultSetHandler for class " + resultSetHandlerClass, e);
        }
    }
}

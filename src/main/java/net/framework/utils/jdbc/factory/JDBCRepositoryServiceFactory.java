package net.framework.utils.jdbc.factory;

import net.framework.annotations.jdbc.Insert;
import net.framework.annotations.jdbc.Select;
import net.framework.utils.jdbc.factory.helpers.JDBCInsertHelper;
import net.framework.utils.jdbc.factory.helpers.JDBCSelectHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public final class JDBCRepositoryServiceFactory {

    @SuppressWarnings(value = "unchecked")
    public static <T> T createRepositoryService(Class<T> interfaceClass) {
        return (T) Proxy
                .newProxyInstance(
                        JDBCRepositoryServiceFactory.class.getClassLoader(),
                        new Class[]{interfaceClass},
                        new JDBCRepositoryInvocationHandler(interfaceClass));
    }

    private static class JDBCRepositoryInvocationHandler implements InvocationHandler {
        private final JDBCSelectHelper selectSQLHelper = new JDBCSelectHelper();
        private final JDBCInsertHelper insertSQLHelper = new JDBCInsertHelper();
        private final Class<?> interfaceClass;

        public <T> JDBCRepositoryInvocationHandler(Class<T> interfaceClass) {
            this.interfaceClass = interfaceClass;
        }

        @Override
        public Object invoke(Object proxy, Method currentMethod, Object[] argsOfCurrentMethod) throws Throwable {
            try {
                Select annotationSelect = currentMethod.getAnnotation(Select.class);
                if (annotationSelect != null) {
                    return selectSQLHelper.getDataOnSelect(annotationSelect, currentMethod, argsOfCurrentMethod);
                }
                Insert annotationInsert = currentMethod.getAnnotation(Insert.class);
                if (annotationInsert != null) {
                    return insertSQLHelper.insertDataToDB(annotationInsert, currentMethod, argsOfCurrentMethod);
                }
                if (currentMethod.getName().equals("toString")) {
                    return "Proxy for " + interfaceClass + " class";
                }
                throw new UnsupportedOperationException("Can't execute currentMethod: " + currentMethod);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }
}

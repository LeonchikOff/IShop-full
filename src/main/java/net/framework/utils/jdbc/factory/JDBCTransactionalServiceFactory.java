package net.framework.utils.jdbc.factory;

import net.framework.TransactionalSynchronizationManager;
import net.framework.annotations.jdbc.Transactional;
import net.framework.exception.FrameworkSystemException;
import net.framework.utils.ReflectionUtils;
import net.framework.utils.jdbc.JDBCConnectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public final class JDBCTransactionalServiceFactory {
    public static Object createTransactionService(DataSource dataSource, Object realService) {
        return Proxy.newProxyInstance(
                JDBCTransactionalServiceFactory.class.getClassLoader(),
                realService.getClass().getInterfaces(),
                new TransactionalServiceInvocationHandler(dataSource, realService));
    }

    public static class TransactionalServiceInvocationHandler implements InvocationHandler {
        private final DataSource dataSource;
        private final Object realService;

        public TransactionalServiceInvocationHandler(DataSource dataSource, Object realService) {
            this.dataSource = dataSource;
            this.realService = realService;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                if (method.getName().equals("toString")) {
                    return "Proxy for " + realService.getClass() + " class";
                } else {
                    Method methodDesired = ReflectionUtils.findMethod(method, realService.getClass());
                    Transactional transactional = ReflectionUtils.findConfigAnnotation(methodDesired, Transactional.class);
                    if (transactional != null) {
                        try (Connection connection = dataSource.getConnection()) {
                            JDBCConnectionUtils.setCurrentConnection(connection);
                            if (!transactional.readOnly()) {
                                try {
                                    TransactionalSynchronizationManager.initTransactionSynchronizationsLocalVariable();
                                    Object resultObj = methodDesired.invoke(realService, args);
                                    connection.commit();
                                    List<TransactionSynchronization> transactionSynchronizationsLocalVariable = TransactionalSynchronizationManager.getTransactionSynchronizationsLocalVariable();
                                    transactionSynchronizationsLocalVariable.forEach(TransactionSynchronization::afterCommit);
                                    return resultObj;
                                } catch (Exception e) {
                                    if (e instanceof RuntimeException) {
                                        connection.rollback();
                                    } else {
                                        connection.commit();
                                    }
                                    throw e;
                                } finally {
                                    TransactionalSynchronizationManager.clearTransactionSynchronizations();
                                }
                            } else {
                                return methodDesired.invoke(realService, args);
                            }
                        } catch (SQLException sqlException) {
                            throw new FrameworkSystemException(sqlException);
                        } finally {
                            JDBCConnectionUtils.removeCurrentConnection();
                        }
                    } else {
                        return methodDesired.invoke(realService, args);
                    }
                }
            } catch (InvocationTargetException invocationTargetException) {
                throw invocationTargetException.getTargetException();
            }
        }

        public Object getRealService() {
            return realService;
        }
    }
}

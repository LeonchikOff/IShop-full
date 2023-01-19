package net.framework.utils.jdbc;

import net.framework.exception.FrameworkSystemException;

import java.sql.Connection;

public final class JDBCConnectionUtils {
    private static final ThreadLocal<Connection> connectionThreadLocal =  new ThreadLocal<>();

    private JDBCConnectionUtils() {
    }

    public static void setCurrentConnection(Connection connection) {
        connectionThreadLocal.set(connection);
    }

    public static Connection getCurrentConnection() {
        Connection connection = connectionThreadLocal.get();
        if(connection == null) {
            throw new FrameworkSystemException("Connection not found for current thread. " +
                    "Does your business service have @Transactional annotation? ");
        }
        return connection;
    }

    public static void removeCurrentConnection() {
        connectionThreadLocal.remove();
    }
}

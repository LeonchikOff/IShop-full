package net.framework.handlers.customized;

import net.framework.handlers.DefaultImplResultSetHandlerForOneRow;
import net.framework.handlers.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DefaultUniqueResultSetHandler<T> implements ResultSetHandler<T> {
    private final ResultSetHandler<T> forOneRowResultSetHandler;

    public DefaultUniqueResultSetHandler(ResultSetHandler<T> forOneRowResultSetHandler) {
        this.forOneRowResultSetHandler = forOneRowResultSetHandler;
    }

    public DefaultUniqueResultSetHandler(Class<T> targetEntityClass) {
        this(new DefaultImplResultSetHandlerForOneRow<>(targetEntityClass));
    }

    @Override
    public T handle(ResultSet resultSet) throws SQLException {
        if(resultSet.next()) {
            return forOneRowResultSetHandler.handle(resultSet);
        } else {
            return null;
        }
    }
}

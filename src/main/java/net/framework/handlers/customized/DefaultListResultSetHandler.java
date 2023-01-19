package net.framework.handlers.customized;

import net.framework.handlers.DefaultImplResultSetHandlerForOneRow;
import net.framework.handlers.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultListResultSetHandler<T> implements ResultSetHandler<List<T>> {
    private final ResultSetHandler<T> forOneRowResultSetHandler;

    public DefaultListResultSetHandler(ResultSetHandler<T> forOneRowResultSetHandler) {
        this.forOneRowResultSetHandler = forOneRowResultSetHandler;
    }

    public DefaultListResultSetHandler(Class<T> targetEntityClass) {
        this(new DefaultImplResultSetHandlerForOneRow<>(targetEntityClass));
    }

    @Override
    public List<T> handle(ResultSet resultSet) throws SQLException {
        List<T> resultObjsList = new ArrayList<>();
        while (resultSet.next()) {
            resultObjsList.add(forOneRowResultSetHandler.handle(resultSet));
        }
        return resultObjsList;
    }
}

package net.framework.handlers;

import net.framework.handlers.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerResultSetHandler implements ResultSetHandler<Integer> {
    @Override
    public Integer handle(ResultSet resultSet) throws SQLException {
        if(resultSet.next()) {
            return resultSet.getInt(1);
        } else {
            return 0;
        }
    }
}

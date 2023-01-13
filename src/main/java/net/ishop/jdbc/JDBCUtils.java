package net.ishop.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JDBCUtils {
    private JDBCUtils() {
    }

    public static <T> T getDataOnSelect(Connection connection, String sql, ResultSetHandler<T> resultSetHandler, Object... parameters) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            populatePreparedStatement(preparedStatement, parameters);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetHandler.handle(resultSet);
        }
    }

    public static <T> T insertDataToDB(Connection connection, String sql, ResultSetHandler<T> resultSetHandler, Object... parameters) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            populatePreparedStatement(preparedStatement, parameters);
            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate != 1) {
                throw new SQLException("Can't insert row to database. Result: " + executeUpdate);
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            return resultSetHandler.handle(generatedKeys);
        }
    }

    public static void insertBatchDataToDB(Connection connection, String sql, List<Object[]> parametersList ) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Object[] objects : parametersList) {
                populatePreparedStatement(preparedStatement, objects);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }


    private static void populatePreparedStatement(PreparedStatement preparedStatement, Object[] parameters) throws SQLException {
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }
        }
    }

    public static void populateSqlAndParams(StringBuilder stringBuilderSQL, List<Object> params, List<Integer> list, String expression) {
        if (list != null && !list.isEmpty()) {
            stringBuilderSQL.append(" AND (");
            for (int i = 0; i < list.size(); i++) {
                stringBuilderSQL.append(expression);
                params.add(list.get(i));
                if (i != list.size() - 1) {
                    stringBuilderSQL.append(" OR ");
                }
            }
            stringBuilderSQL.append(" )");
        }
    }
}

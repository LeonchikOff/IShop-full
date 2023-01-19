package net.framework.utils;

import net.framework.exception.FrameworkSystemException;
import net.framework.handlers.ResultSetHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCUtils {
    private JDBCUtils() {
    }

    public static <T> T getDataOnSelect(Connection connection, String sql, ResultSetHandler<T> resultSetHandler, Object... parameters) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            populatePreparedStatement(preparedStatement, parameters);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetHandler.handle(resultSet);
        } catch (SQLException sqlException) {
            throw new FrameworkSystemException("Can't execute query: " + sqlException.getMessage(), sqlException);
        }
    }

    public static <T> T insertDataToDB(Connection connection, String sql, ResultSetHandler<T> resultSetHandler, Object... parameters) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            populatePreparedStatement(preparedStatement, parameters);
            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate != 1) {
                throw new SQLException("Can't insert row to database. Result: " + executeUpdate);
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            return resultSetHandler.handle(generatedKeys);
        } catch (SQLException sqlException) {
            throw new FrameworkSystemException("Can't execute query: " + sqlException.getMessage(), sqlException);
        }
    }

    private static void populatePreparedStatement(PreparedStatement preparedStatement, Object[] parameters) throws SQLException {
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }
        }
    }


    //    public static void insertBatchDataToDB(Connection connection, String sql, List<Object[]> parametersList ) {
//        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            for (Object[] objects : parametersList) {
//                populatePreparedStatement(preparedStatement, objects);
//                preparedStatement.addBatch();
//            }
//            preparedStatement.executeBatch();
//        } catch (SQLException sqlException) {
//            throw new FrameworkSystemException("Can't execute query: " + sqlException.getMessage(), sqlException);
//        }
//    }
}

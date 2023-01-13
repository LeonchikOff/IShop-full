package net.ishop.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

//transformation of the received resultSet, upon request to the database, into the corresponding object.
public interface ResultSetHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}

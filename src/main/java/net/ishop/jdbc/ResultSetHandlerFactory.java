package net.ishop.jdbc;

import net.ishop.entities.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//ORM
public class ResultSetHandlerFactory {
    public static final ResultSetHandler<Account> ACCOUNT_RESULT_SET_HANDLER = resultSet -> {
        Account account = new Account();
        account.setId(resultSet.getInt("id"));
        account.setName(resultSet.getString("name"));
        account.setEmail(resultSet.getString("email"));
        return account;
    };

    //Handler ResultSet to Product
    public final static ResultSetHandler<Product> PRODUCT_RESULT_SET_HANDLER = new ResultSetHandler<Product>() {
        @Override
        public Product handle(ResultSet resultSet) throws SQLException {
            Product product = new Product();
            product.setId(resultSet.getInt("id"));
            product.setName(resultSet.getString("name"));
            product.setPrice(resultSet.getBigDecimal("price"));
            product.setCategoryName(resultSet.getString("category"));
            product.setProducerName(resultSet.getString("producer"));
            product.setDescription(resultSet.getString("description"));
            product.setImageLink(resultSet.getString("image_link"));
            return product;
        }
    };

    //Handler ResultSet to Category
    public final static ResultSetHandler<Category> CATEGORY_RESULT_SET_HANDLER = new ResultSetHandler<Category>() {
        @Override
        public Category handle(ResultSet resultSet) throws SQLException {
            Category category = new Category();
            category.setId(resultSet.getInt("id"));
            category.setName(resultSet.getString("name"));
            category.setUrl(resultSet.getString("url"));
            category.setProductCount(resultSet.getInt("product_count"));
            return category;
        }
    };
    //Handler ResultSet to Producer
    public final static ResultSetHandler<Producer> PRODUCER_RESULT_SET_HANDLER = new ResultSetHandler<Producer>() {
        @Override
        public Producer handle(ResultSet resultSet) throws SQLException {
            Producer producer = new Producer();
            producer.setId(resultSet.getInt("id"));
            producer.setName(resultSet.getString("name"));
            producer.setProductCount(resultSet.getInt("product_count"));
            return producer;
        }
    };

    public static final ResultSetHandler<Order> ORDER_RESULT_SET_HANDLER = resultSet -> {
        Order order = new Order();
        order.setId(resultSet.getLong("id"));
        order.setIdAccount(resultSet.getInt("id_account"));
        order.setDateOfCreated(resultSet.getTimestamp("date_of_created"));
        return order;
    };

    public static final ResultSetHandler<OrderItem> ORDER_ITEM_RESULT_SET_HANDLER = resultSet -> {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(resultSet.getLong("order_item_id"));
        orderItem.setIdOrder(resultSet.getLong("id_order"));
        Product product = PRODUCT_RESULT_SET_HANDLER.handle(resultSet);
        orderItem.setProduct(product);
        orderItem.setCount(resultSet.getInt("count"));
        return orderItem;
    };

    private ResultSetHandlerFactory() {
    }

    // тут получение списка обработчиков обьектов из бд
    public static <T> ResultSetHandler<List<T>> getListResultSetHandler(ResultSetHandler<T> resultSetHandlerForOneRowFromDB) {
        return new ResultSetHandler<List<T>>() {
            @Override
            public List<T> handle(ResultSet resultSet) throws SQLException {
                ArrayList<T> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(resultSetHandlerForOneRowFromDB.handle(resultSet));
                }
                return list;
            }
        };

    }

    // тут получение обработчика одной строки из БД (то есть обработчика одного объекта из бд)
    public static <T> ResultSetHandler<T> getSingleResultSetHandler(ResultSetHandler<T> resultSetHandlerForOneRowFromDB) {
        return new ResultSetHandler<T>() {
            @Override
            public T handle(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    return resultSetHandlerForOneRowFromDB.handle(resultSet);
                } else return null;
            }
        };
    }

    public static ResultSetHandler<Integer> getCountResultSetHandler() {
        return resultSet -> {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else return 0;
        };
    }

}

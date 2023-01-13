package net.ishop.services.impl;

import net.ishop.entities.Account;
import net.ishop.entities.Order;
import net.ishop.entities.OrderItem;
import net.ishop.entities.Product;
import net.ishop.exceptions.AccessDeniedException;
import net.ishop.exceptions.InternalServerErrorException;
import net.ishop.exceptions.ResourceNotFoundException;
import net.ishop.jdbc.JDBCUtils;
import net.ishop.jdbc.ResultSetHandler;
import net.ishop.jdbc.ResultSetHandlerFactory;
import net.ishop.models.ShoppingCart;
import net.ishop.models.ShoppingCartItem;
import net.ishop.models.forms.ProductForm;
import net.ishop.models.social.CurrentAccount;
import net.ishop.models.social.SocialAccount;
import net.ishop.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private static final ResultSetHandler<Product> productResultSetHandler =
            ResultSetHandlerFactory.getSingleResultSetHandler(ResultSetHandlerFactory.PRODUCT_RESULT_SET_HANDLER);
    public static final ResultSetHandler<Account> accountResultSetHandler =
            ResultSetHandlerFactory.getSingleResultSetHandler(ResultSetHandlerFactory.ACCOUNT_RESULT_SET_HANDLER);
    public static final ResultSetHandler<Order> orderResultSetHandler =
            ResultSetHandlerFactory.getSingleResultSetHandler(ResultSetHandlerFactory.ORDER_RESULT_SET_HANDLER);
    public static final ResultSetHandler<List<Order>> ordersResultSetHandler
            = ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.ORDER_RESULT_SET_HANDLER);
    public static final ResultSetHandler<List<OrderItem>> orderItemsResultSetHandler =
            ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.ORDER_ITEM_RESULT_SET_HANDLER);
    public static final ResultSetHandler<Integer> countOrdersResultSetHandler =
            ResultSetHandlerFactory.getCountResultSetHandler();


    private final DataSource dataSource;

    public OrderServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public CurrentAccount authenticateViaDB(SocialAccount socialAccount) {
        try (Connection connection = this.dataSource.getConnection()) {
            String sqlSelect = "SELECT * FROM account where email = ?";
            Account account = JDBCUtils.getDataOnSelect(
                    connection, sqlSelect, accountResultSetHandler, socialAccount.getEmail()
            );
            if (account == null) {
                account = new Account(socialAccount.getName(), socialAccount.getEmail());
                String sqlInsert = "INSERT INTO account VALUES (nextval('account_seq'),?,?)";
                account = JDBCUtils.insertDataToDB(
                        connection, sqlInsert, accountResultSetHandler, account.getName(), account.getEmail()
                );
//                connection.commit();
            }
            return account;
        } catch (SQLException sqlException) {
            throw new InternalServerErrorException("Can't execute SQL request: " + sqlException.getMessage(), sqlException);
        }
    }

    @Override
    public void addProductToShoppingCart(ProductForm productForm, ShoppingCart shoppingCart) {
        try (Connection connection = dataSource.getConnection()) {
            String sql =
                    "SELECT p.*, ctr.name AS category, pr.name AS producer FROM product p, category ctr, producer pr " +
                            "WHERE p.id_category = ctr.id AND p.id_producer = pr.id  AND p.id=?";
            Product product = JDBCUtils.getDataOnSelect(connection, sql, productResultSetHandler, productForm.getIdProduct());
            if (product == null) {
                throw new InternalServerErrorException("Product not found by id: " + productForm.getIdProduct());
            }
            shoppingCart.addProduct(product, productForm.getCount());
        } catch (SQLException sqlException) {
            throw new InternalServerErrorException("Can't execute sql query: " + sqlException.getMessage(), sqlException);
        }
    }

    @Override
    public void removeProductFromShoppingCart(ProductForm productForm, ShoppingCart shoppingCart) {
        shoppingCart.removeProduct(productForm.getIdProduct(), productForm.getCount());
    }

    @Override
    public long makeOrderAndGetId(ShoppingCart shoppingCart, CurrentAccount currentAccount) {
        if (shoppingCart == null || shoppingCart.getShoppingCartItems().isEmpty()) {
            throw new InternalServerErrorException("Shopping cart is empty");
        }
        try (Connection connection = dataSource.getConnection()) {
            String sqlInsertOrder = "INSERT INTO \"order\" VALUES (nextval('order_seq'),?,?)";

            Order order = JDBCUtils.insertDataToDB(
                    connection, sqlInsertOrder, orderResultSetHandler, new Timestamp(System.currentTimeMillis()), currentAccount.getId());

            String sqlInsertOrderItem = "INSERT INTO order_item VALUES (nextval('order_item_seq'),?,?,?)";
            List<Object[]> orderItemParameterList = toOrderItemParameterList(order.getId(), shoppingCart.getShoppingCartItems());

            JDBCUtils.insertBatchDataToDB(connection, sqlInsertOrderItem, orderItemParameterList);
//            connection.commit();
            return order.getId();
        } catch (SQLException sqlException) {
            throw new InternalServerErrorException("Can't execute SQL request: " + sqlException.getMessage(), sqlException);
        }
    }

    @Override
    public Order findOrderById(long orderId, CurrentAccount currentAccount) {
        try (Connection connection = dataSource.getConnection()) {
            String sqlSelectForOrder = "SELECT * FROM \"order\" WHERE id=?";
            Order order = JDBCUtils.getDataOnSelect(connection, sqlSelectForOrder, orderResultSetHandler, orderId);
            if (order == null) {
                throw new ResourceNotFoundException("Order not found by id: " + orderId);
            }
            if (!order.getIdAccount().equals(currentAccount.getId())) {
                throw new AccessDeniedException("Account with id: " + currentAccount.getId() + " is not owner for order with id: " + orderId);
            }

            String sqlSelectForOrderItems =
                    "SELECT " +
                            "oi.id as order_item_id, " +
                            "oi.id_order as id_order, " +
                            "p.*, " +
                            "ctr.name as category, " +
                            "pr.name  as producer, " +
                            "oi.count " +
                            "FROM order_item oi, product p, category ctr, producer pr " +
                            "WHERE p.id = oi.id_product and p.id_category = ctr.id and p.id_producer = pr.id and oi.id_order = ?;";

            List<OrderItem> orderItems = JDBCUtils.getDataOnSelect(connection, sqlSelectForOrderItems, orderItemsResultSetHandler, orderId);
            order.setOrderItemsList(orderItems);
            return order;
        } catch (SQLException sqlException) {
            throw new InternalServerErrorException("Can't execute SQL request: " + sqlException.getMessage(), sqlException);
        }
    }

    @Override
    public List<Order> getListMyOrders(CurrentAccount currentAccount, int numberOfPage, int limit) {
        int offSet = (numberOfPage - 1) * limit;
        try (Connection connection = dataSource.getConnection()) {
            String sqlSelectOrder = "SELECT * FROM \"order\" WHERE id_account = ? ORDER BY id DESC LIMIT ? OFFSET ?;";
            return JDBCUtils.getDataOnSelect(connection, sqlSelectOrder, ordersResultSetHandler, currentAccount.getId(), limit, offSet);
        } catch (SQLException sqlException) {
            throw new InternalServerErrorException("Can't execute SQL request: " + sqlException.getMessage(), sqlException);
        }
    }

    @Override
    public int getCountMyOrders(CurrentAccount currentAccount) {
        try (Connection connection = dataSource.getConnection()) {
            String sqlSelectCountOfOrders = "SELECT count(*) FROM \"order\" WHERE id_account = ?";
            return JDBCUtils.getDataOnSelect(connection, sqlSelectCountOfOrders, countOrdersResultSetHandler, currentAccount.getId());
        } catch (SQLException sqlException) {
            throw new InternalServerErrorException("Can't execute SQL request: " + sqlException.getMessage(), sqlException);
        }
    }

    private List<Object[]> toOrderItemParameterList(long idOrder, Collection<ShoppingCartItem> cartItems) {
        ArrayList<Object[]> parametersList = new ArrayList<>();
        cartItems.forEach(shoppingCartItem -> parametersList.add(
                new Object[]
                        {
                                idOrder,
                                shoppingCartItem.getProduct().getId(),
                                shoppingCartItem.getQuantityOfUniqueProduct(),
                        })
        );
        return parametersList;
    }

    public String serializeShoppingCart(ShoppingCart shoppingCart) {
        StringBuilder stringBuilder = new StringBuilder();
        shoppingCart.getShoppingCartItems().forEach(shoppingCartItem -> {
            stringBuilder
                    .append(shoppingCartItem.getProduct().getId()).append("-")
                    .append(shoppingCartItem.getQuantityOfUniqueProduct()).append("|");
        });
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    public ShoppingCart deserializeShoppingCart(String str) {
        ShoppingCart shoppingCart = new ShoppingCart();
        String[] shoppingCartItems = str.split("[|]");
        for (String shoppingCartItemStr : shoppingCartItems) {
            try {
                String[] dataItem = shoppingCartItemStr.split("[-]");
                int idProduct = Integer.parseInt(dataItem[0]);
                int count = Integer.parseInt(dataItem[1]);
                this.addProductToShoppingCart(new ProductForm(idProduct, count), shoppingCart);
            } catch (RuntimeException runtimeException) {
                LOGGER.error("Can't add product to Shopping Cart during deserialization: item= " + shoppingCartItemStr, runtimeException);
            }
        }
        return shoppingCart.getShoppingCartItems().isEmpty() ? null : shoppingCart;
    }
}

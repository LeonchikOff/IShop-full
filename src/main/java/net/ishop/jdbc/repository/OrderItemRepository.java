package net.ishop.jdbc.repository;

import net.framework.annotations.jdbc.Insert;
import net.framework.annotations.jdbc.Select;
import net.framework.annotations.jdbc.mapping.CollectionItem;
import net.ishop.entities.OrderItem;

import java.util.List;

public interface OrderItemRepository {
    String QUERY_ORDER_ITEMS_BY_ORDER_ID = "SELECT oi.id, oi.id_order as id_order, p.*, p.id as id_product, p.name, price, description, image_link, " +
            "ctr.name as category, pr.name as producer, oi.count " +
            "FROM order_item oi, product p, category ctr, producer pr " +
            "WHERE oi.id_product = p.id " +
            "and p.id_category = ctr.id " +
            "and p.id_producer = pr.id " +
            "and oi.id_order = ?";

    String QUERY_ORDER_ITEMS_BY_ORDER_ID2 = " SELECT oi.id, oi.count, oi.id_order, oi.id_product," +
            "    p.name, p.price, p.description, p.image_link," +
            "    p.id," +
            "    ctr.name    as category_name," +
            "    pr.name     as producer_name" +
            "    FROM product p," +
            "    order_item oi," +
            "    category ctr," +
            "    producer pr" +
            "    WHERE p.id_category = ctr.id" +
            "    and p.id_producer = pr.id" +
            "    and oi.id_product = p.id" +
            "    and oi.id_order = ?";



    @Insert
    void createOrderItem(OrderItem orderItem);

    @Select(sqlQuery = QUERY_ORDER_ITEMS_BY_ORDER_ID2)
    @CollectionItem(parameterizedClass = OrderItem.class)
    List<OrderItem> findOrderItemsByOrderId(Long orderId);

}

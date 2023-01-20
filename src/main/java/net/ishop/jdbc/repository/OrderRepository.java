package net.ishop.jdbc.repository;

import net.framework.annotations.dependency_injection.JDBCRepository;
import net.framework.annotations.jdbc.Insert;
import net.framework.annotations.jdbc.Select;
import net.framework.annotations.jdbc.mapping.CollectionItem;
import net.ishop.entities.Order;

import java.util.List;

@JDBCRepository
public interface OrderRepository {

    @Insert
    void createOrder(Order order);

    @Select(sqlQuery = "SELECT * FROM public.order WHERE id=?")
    Order findOrderById(Long orderId);

    @Select(sqlQuery = "SELECT * FROM public.order WHERE id_account = ? ORDER BY id DESC LIMIT ? OFFSET ?")
    @CollectionItem(parameterizedClass = Order.class)
    List<Order> findOrdersByAccountId(Integer accountId, int limit, int offSet);

    @Select(sqlQuery = "SELECT count(*) FROM public.order WHERE id_account = ?")
    int getCountOrdersByAccountId(Integer accountId);
}

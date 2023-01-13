package net.ishop.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Order extends AbstractEntity<Long> {
    private Integer idAccount;
    private Timestamp dateOfCreated;
    private List<OrderItem> orderItemsList;

    public Integer getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Integer idAccount) {
        this.idAccount = idAccount;
    }

    public Timestamp getDateOfCreated() {
        return dateOfCreated;
    }

    public void setDateOfCreated(Timestamp dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }

    public List<OrderItem> getOrderItemsList() {
        return orderItemsList;
    }

    public void setOrderItemsList(List<OrderItem> orderItemsList) {
        this.orderItemsList = orderItemsList;
    }

    public BigDecimal totalCost() {
        BigDecimal cost = BigDecimal.ZERO;
        if (orderItemsList != null) {
            for (OrderItem orderItem : orderItemsList) {
                cost = cost.add(orderItem.getProduct().getPrice())
                        .multiply(BigDecimal.valueOf(orderItem.getCount()));
            }
        }
        return cost;
    }

    @Override
    public String toString() {
        return "Order{" +
                "idAccount=" + idAccount +
                ", dateOfCreated=" + dateOfCreated +
                ", orderItemsList=" + orderItemsList +
                ", id=" + id +
                '}';
    }
}

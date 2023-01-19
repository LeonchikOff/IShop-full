package net.ishop.entities;

import net.framework.annotations.jdbc.mapping.Column;
import net.framework.annotations.jdbc.mapping.Table;
import net.framework.annotations.jdbc.mapping.Transient;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Table(nameOfTable = "\"order\"", generationNextIdExpresion = "nextval('order_seq')")
public class Order extends AbstractEntity<Long> {
    @Column(columnName = "date_of_created")
    private Timestamp dateOfCreated;
    @Column(columnName = "id_account")
    private Integer idAccount;
    @Transient
    private List<OrderItem> orderItemsList;

    public Order() {
    }

    public Order(Timestamp dateOfCreated, Integer idAccount) {
        this.dateOfCreated = dateOfCreated;
        this.idAccount = idAccount;
    }

    public Timestamp getDateOfCreated() {
        return dateOfCreated;
    }
    public Integer getIdAccount() {
        return idAccount;
    }
    public List<OrderItem> getOrderItemsList() {
        return orderItemsList;
    }
    public void setDateOfCreated(Timestamp dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }
    public void setIdAccount(Integer idAccount) {
        this.idAccount = idAccount;
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

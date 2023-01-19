package net.ishop.entities;

import net.framework.annotations.jdbc.mapping.JoinTable;
import net.framework.annotations.jdbc.mapping.Column;
import net.framework.annotations.jdbc.mapping.Table;

@Table(nameOfTable = "order_item", generationNextIdExpresion = "nextval('order_item_seq')")
public class OrderItem extends AbstractEntity<Long> {
    @Column(columnName = "id_order")
    private Long idOrder;
    @JoinTable(columnName = "id_product", nameOfFieldBeJoined = "id")
    private Product product;
    @Column(columnName = "count")
    private Integer count;

    public OrderItem() {
    }

    public OrderItem(Long idOrder, Product product, Integer count) {
        this.idOrder = idOrder;
        this.product = product;
        this.count = count;
    }

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "idOrder=" + idOrder +
                ", product=" + product +
                ", count=" + count +
                ", id=" + id +
                '}';
    }
}

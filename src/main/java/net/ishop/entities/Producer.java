package net.ishop.entities;

import net.framework.annotations.jdbc.mapping.Column;
import net.framework.annotations.jdbc.mapping.Table;

@Table(nameOfTable = "producer")
public class Producer extends AbstractEntity<Integer> {
    @Column(columnName = "name")
    private String name;
    @Column(columnName = "product_count")
    private Integer productCount;

    public Producer() {
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }
    public String getName() {
        return name;
    }
    public Integer getProductCount() {
        return productCount;
    }

    @Override
    public String toString() {
        return "Producer{" +
                "name='" + name + '\'' +
                ", productCount=" + productCount +
                ", id=" + id +
                '}';
    }
}

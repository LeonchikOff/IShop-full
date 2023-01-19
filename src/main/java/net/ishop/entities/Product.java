package net.ishop.entities;

import net.framework.annotations.jdbc.mapping.Column;
import net.framework.annotations.jdbc.mapping.Table;

import java.math.BigDecimal;

@Table(nameOfTable = "product")
public class Product extends AbstractEntity<Integer> {
    @Column(columnName = "name")
    private String name;
    @Column(columnName = "price")
    private BigDecimal price;
    @Column(columnName = "description")
    private String description;
    @Column(columnName = "image_link")
    private String imageLink;

    @Column(columnName = "category_name")
    private String categoryName;
    @Column(columnName = "producer_name")
    private String producerName;

    public Product() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageLink='" + imageLink + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", producerName='" + producerName + '\'' +
                ", id=" + id +
                '}';
    }
}

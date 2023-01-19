package net.ishop.entities;

import net.framework.annotations.jdbc.mapping.Column;
import net.framework.annotations.jdbc.mapping.Table;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Table(nameOfTable = "category" )
public class Category extends AbstractEntity<Integer> {
    @Column(columnName = "name")
    private String name;
    @Column(columnName = "url")
    private String url;
    @Column(columnName = "product_count")
    private Integer productCount;

    public Category() {
    }

    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public Integer getProductCount() {
        return productCount;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", productCount=" + productCount +
                ", id=" + id +
                '}';
    }
}

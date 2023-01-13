package net.ishop.entities;

public class Producer extends AbstractEntity<Integer> {
    private String name;
    private Integer productCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
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

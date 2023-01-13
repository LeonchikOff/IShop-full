package net.ishop.models;

import net.ishop.entities.Product;

import java.io.Serializable;

public class ShoppingCartItem implements Serializable {
    private Product product;
    private int quantityOfUniqueProduct;

    public ShoppingCartItem() {
    }

    public ShoppingCartItem(Product product, int quantityOfUniqueProduct) {
        this.product = product;
        this.quantityOfUniqueProduct = quantityOfUniqueProduct;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantityOfUniqueProduct() {
        return quantityOfUniqueProduct;
    }

    public void setQuantityOfUniqueProduct(int quantityOfUniqueProduct) {
        this.quantityOfUniqueProduct = quantityOfUniqueProduct;
    }

    @Override
    public String toString() {
        return "ShoppingCartItem{" +
                "product=" + product +
                ", quantityOfUniqueProduct=" + quantityOfUniqueProduct +
                '}';
    }
}

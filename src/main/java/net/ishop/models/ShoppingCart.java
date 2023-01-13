package net.ishop.models;

import net.ishop.entities.Product;
import net.ishop.exceptions.ValidationException;
import net.ishop.models.constants.Constants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCart implements Serializable {
    private final Map<Integer, ShoppingCartItem> shoppingCartItemMap = new LinkedHashMap<>();
    private int totalCountOfAllProducts;
    private BigDecimal totalCostOfAllProducts = BigDecimal.ZERO;

    public void addProduct(Product product, int quantityOfUniqueProduct) {
        ShoppingCartItem shoppingCartItem = shoppingCartItemMap.get(product.getId());
        if (shoppingCartItem != null) {
            int updateQuantity = shoppingCartItem.getQuantityOfUniqueProduct() + quantityOfUniqueProduct;
            validateQuantityOfUniqueProduct(updateQuantity);
            shoppingCartItem.setQuantityOfUniqueProduct(updateQuantity);
        } else {
            validateShoppingCartSize(product.getId());
            validateQuantityOfUniqueProduct(quantityOfUniqueProduct);
            shoppingCartItem = new ShoppingCartItem(product, quantityOfUniqueProduct);
            shoppingCartItemMap.put(product.getId(), shoppingCartItem);
        }
        refreshStatistics();
    }

    public void removeProduct(Integer idProduct, int quantityOfUniqueProduct) {
        ShoppingCartItem shoppingCartItem = shoppingCartItemMap.get(idProduct);
        if (shoppingCartItem != null) {
            if (shoppingCartItem.getQuantityOfUniqueProduct() > quantityOfUniqueProduct) {
                shoppingCartItem.setQuantityOfUniqueProduct(shoppingCartItem.getQuantityOfUniqueProduct() - quantityOfUniqueProduct);
            } else shoppingCartItemMap.remove(idProduct);
            refreshStatistics();
        }
    }

    private void validateShoppingCartSize(int idProduct) {
        if (shoppingCartItemMap.size() > Constants.MAX_COUNT_PRODUCTS_PER_SHOPPING_CART
                || shoppingCartItemMap.size() == Constants.MAX_COUNT_PRODUCTS_PER_SHOPPING_CART && !shoppingCartItemMap.containsKey(idProduct)) {
            throw new ValidationException("The size limit for the current shopping cart has been reached: size = " + shoppingCartItemMap.size());
        }
    }

    private void validateQuantityOfUniqueProduct(int quantityOfUniqueProduct) {
        if (quantityOfUniqueProduct > Constants.MAX_QUANTITY_OF_UNIQUE_PRODUCT_PER_ONE_SHOPPING_CART) {
            throw new ValidationException("Exceeding the limit on quantity of unique product in the current shopping cart: requested quantity = " + quantityOfUniqueProduct);
        }
    }

    private void refreshStatistics() {
        totalCountOfAllProducts = 0;
        totalCostOfAllProducts = BigDecimal.ZERO;
        shoppingCartItemMap.values().forEach(shoppingCartItem -> {
            int quantityOfUniqueProduct = shoppingCartItem.getQuantityOfUniqueProduct();
            totalCountOfAllProducts += quantityOfUniqueProduct;
            totalCostOfAllProducts = totalCostOfAllProducts.add(
                    shoppingCartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(quantityOfUniqueProduct)));
        });
    }

    public Collection<ShoppingCartItem> getShoppingCartItems() {
        return shoppingCartItemMap.values();
    }

    public int getTotalCountOfAllProducts() {
        return totalCountOfAllProducts;
    }

    public BigDecimal getTotalCostOfAllProducts() {
        return totalCostOfAllProducts;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "shoppingCartItemMap=" + shoppingCartItemMap +
                ", totalCountOfAllProducts=" + totalCountOfAllProducts +
                ", totalCostOfAllProducts=" + totalCostOfAllProducts +
                '}';
    }
}

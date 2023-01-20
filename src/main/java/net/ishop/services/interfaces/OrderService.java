package net.ishop.services.interfaces;

import net.ishop.entities.Order;
import net.ishop.models.ShoppingCart;
import net.ishop.models.forms.ProductForm;
import net.ishop.models.social.CurrentAccount;
import net.ishop.models.social.SocialAccount;

import java.util.List;

public interface OrderService {
    CurrentAccount authenticateViaDB(SocialAccount socialAccount);
    String serializeShoppingCart(ShoppingCart shoppingCart);
    ShoppingCart deserializeShoppingCart(String str);
    void addProductToShoppingCart(ProductForm productForm, ShoppingCart shoppingCart);
    void removeProductFromShoppingCart(ProductForm productForm, ShoppingCart shoppingCart);
    long makeOrderAndGetId(ShoppingCart shoppingCart, CurrentAccount currentAccount);
    Order findOrderById(long orderId, CurrentAccount currentAccount);
    List<Order> getListOrdersForCurrentAccount(CurrentAccount currentAccount, int numberOfPage, int limit);
    int getCountOfOrdersForCurrentAccount(CurrentAccount currentAccount);
}

package net.ishop.services.interfaces;

import net.ishop.models.ShoppingCartItem;
import net.ishop.models.forms.ProductForm;

import java.util.Collection;
import java.util.List;

public interface CookieService {
    String createdShoppingCartCookie(Collection<ShoppingCartItem> cartItems);
    List<ProductForm> parseChoppingCartCookie(String cookieValue);
}

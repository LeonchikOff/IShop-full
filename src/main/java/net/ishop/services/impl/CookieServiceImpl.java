package net.ishop.services.impl;

import net.framework.annotations.dependency_injection.Component;
import net.ishop.models.ShoppingCartItem;
import net.ishop.models.forms.ProductForm;
import net.ishop.services.interfaces.CookieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CookieServiceImpl implements CookieService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CookieServiceImpl.class);

    @Override
    public String createdShoppingCartCookie(Collection<ShoppingCartItem> cartItems) {
        StringBuilder stringBuilder = new StringBuilder();
        cartItems.forEach(shoppingCartItem -> {
            stringBuilder
                    .append(shoppingCartItem.getProduct().getId()).append("-")
                    .append(shoppingCartItem.getQuantityOfUniqueProduct()).append("|");
        });
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    @Override
    public List<ProductForm> parseChoppingCartCookie(String cookieValue) {
        List<ProductForm> productForms = new ArrayList<>();
        String[] shoppingCartItems = cookieValue.split("[|]");
        for (String shoppingCartItemStr : shoppingCartItems) {
            try {
                String[] dataItem = shoppingCartItemStr.split("[-]");
                int idProduct = Integer.parseInt(dataItem[0]);
                int count = Integer.parseInt(dataItem[1]);
                productForms.add(new ProductForm(idProduct, count));
            } catch (RuntimeException runtimeException) {
                LOGGER.error("Can't parse cookie value: item = " + shoppingCartItemStr + ", cookieValue = " + cookieValue,
                        runtimeException);
            }
        }
        return productForms;
    }
}

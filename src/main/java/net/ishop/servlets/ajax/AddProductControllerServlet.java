package net.ishop.servlets.ajax;

import net.ishop.models.ShoppingCart;
import net.ishop.models.forms.ProductForm;
import net.ishop.servlets.AbstractProductControllerServlet;
import net.ishop.utils.SessionShoppingCartUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ajax/json/product/add")
public class AddProductControllerServlet extends AbstractProductControllerServlet {

    @Override
    protected void processProductForm(ProductForm productForm, ShoppingCart currentShoppingCart, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.getOrderService().addProductToShoppingCart(productForm, currentShoppingCart);
        String cookieValue = this.getOrderService().serializeShoppingCart(currentShoppingCart);
        SessionShoppingCartUtils.updateCurrentShoppingCartCookie(resp, cookieValue);
    }
}

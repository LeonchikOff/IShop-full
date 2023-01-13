package net.ishop.servlets;


import net.ishop.models.ShoppingCart;
import net.ishop.models.constants.Constants;
import net.ishop.models.forms.ProductForm;
import net.ishop.utils.RoutingUtils;
import net.ishop.utils.SessionShoppingCartUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractProductControllerServlet extends AbstractControllerServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductForm productForm = createProductForm(req);
        ShoppingCart currentShoppingCart = getCurrentShoppingCart(req);
        processProductForm(productForm, currentShoppingCart, req, resp);
        if (!SessionShoppingCartUtils.isCurrentShoppingCartCreated(req)) {
            SessionShoppingCartUtils.setCurrentShoppingCart(req, currentShoppingCart);
        }
        sendResponse(currentShoppingCart, req, resp);
    }

    private static ShoppingCart getCurrentShoppingCart(HttpServletRequest req) {
        ShoppingCart shoppingCart = SessionShoppingCartUtils.getCurrentShoppingCart(req);
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
        }
        return shoppingCart;
    }

    protected abstract void processProductForm(ProductForm productForm,
                                               ShoppingCart currentShoppingCart,
                                               HttpServletRequest req,
                                               HttpServletResponse resp)
            throws ServletException, IOException;

    private void sendResponse(ShoppingCart currentShoppingCart, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject cartStatisticsJSON = new JSONObject();
        cartStatisticsJSON.put("totalCount", currentShoppingCart.getTotalCountOfAllProducts());
        cartStatisticsJSON.put("totalCost", currentShoppingCart.getTotalCostOfAllProducts());
        RoutingUtils.sendJSON(cartStatisticsJSON, resp);
    }

}

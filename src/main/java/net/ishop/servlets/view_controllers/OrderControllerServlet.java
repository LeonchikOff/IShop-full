package net.ishop.servlets.view_controllers;

import net.ishop.entities.Order;
import net.ishop.models.ShoppingCart;
import net.ishop.models.social.CurrentAccount;
import net.ishop.servlets.AbstractControllerServlet;
import net.ishop.utils.RoutingUtils;
import net.ishop.utils.SessionAccountUtils;
import net.ishop.utils.SessionShoppingCartUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/order")
public class OrderControllerServlet extends AbstractControllerServlet {
    public static final String CURRENT_MESSAGE = "CURRENT_MESSAGE";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ShoppingCart currentShoppingCart = SessionShoppingCartUtils.getCurrentShoppingCart(req);
        CurrentAccount currentAccount = SessionAccountUtils.getCurrentAccount(req);
        long idOrder = this.getOrderService().makeOrderAndGetId(currentShoppingCart, currentAccount);
        SessionShoppingCartUtils.clearCurrentShoppingCart(req, resp);

        String msg = "Order created successfully. Please wait for our reply.";
        req.getSession().setAttribute(CURRENT_MESSAGE, msg);

        RoutingUtils.redirect(resp, "/order?id=" + idOrder);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String msg = (String) req.getSession().getAttribute(CURRENT_MESSAGE);
        req.getSession().removeAttribute(CURRENT_MESSAGE);
        req.setAttribute(CURRENT_MESSAGE, msg);
        Order orderById = this.getOrderService().findOrderById(Long.parseLong(req.getParameter("id")), SessionAccountUtils.getCurrentAccount(req));
        BigDecimal totalCost = orderById.totalCost();
        req.setAttribute("orderTotalCost", totalCost);
        req.setAttribute("order", orderById);
        RoutingUtils.forwardToPageTemplate("order.jsp", req, resp);
    }
}

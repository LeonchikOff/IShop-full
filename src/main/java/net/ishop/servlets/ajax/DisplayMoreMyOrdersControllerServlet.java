package net.ishop.servlets.ajax;

import net.ishop.entities.Order;
import net.ishop.models.constants.Constants;
import net.ishop.servlets.AbstractControllerServlet;
import net.ishop.utils.RoutingUtils;
import net.ishop.utils.SessionAccountUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ajax/html/more/my_orders")
public class DisplayMoreMyOrdersControllerServlet extends AbstractControllerServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Order> listMyOrders = this.getOrderService().getListOrdersForCurrentAccount(
                        SessionAccountUtils.getCurrentAccount(req), this.getCurrentPage(req), Constants.COUNT_ORDERS_PER_PAGE);
        req.setAttribute("listMyOrders", listMyOrders);
        RoutingUtils.forwardToFragment("my_orders_tbody.jsp", req, resp);
    }
}

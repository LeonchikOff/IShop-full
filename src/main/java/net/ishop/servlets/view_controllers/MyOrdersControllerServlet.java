package net.ishop.servlets.view_controllers;

import net.ishop.entities.Order;
import net.ishop.models.constants.Constants;
import net.ishop.models.social.CurrentAccount;
import net.ishop.servlets.AbstractControllerServlet;
import net.ishop.utils.SessionAccountUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/my_orders")
public class MyOrdersControllerServlet extends AbstractControllerServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrentAccount currentAccount = SessionAccountUtils.getCurrentAccount(req);
        List<Order> listMyOrders = this.getOrderService()
                .getListOrdersForCurrentAccount(currentAccount, 1, Constants.COUNT_ORDERS_PER_PAGE);
        req.setAttribute("listMyOrders", listMyOrders);
        int countMyOrders = this.getOrderService().getCountOfOrdersForCurrentAccount(currentAccount);
        req.setAttribute("countPage", this.getCountPage(countMyOrders, Constants.COUNT_ORDERS_PER_PAGE));

//        RoutingUtils.forwardToPageTemplate("my_orders.jsp", req, resp);
        req.setAttribute("dynamicLoadingPage", "pages/my_orders.jsp");
        req.getRequestDispatcher("/WEB-INF/jsp/page-template.jsp").forward(req, resp);
    }
}

package net.ishop.servlets.view_controllers;

import net.ishop.servlets.AbstractControllerServlet;
import net.ishop.utils.SessionShoppingCartUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/shopping-cart")
public class ShowShoppingCartControllerServlet extends AbstractControllerServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(SessionShoppingCartUtils.isCurrentShoppingCartCreated(req)) {
//        RoutingUtils.forwardToPageTemplate("shopping_cart.jsp", req, resp);
            req.setAttribute("dynamicLoadingPage", "pages/shopping_cart.jsp");
            req.getRequestDispatcher("/WEB-INF/jsp/page-template.jsp").forward(req, resp);
        } else {
//            RoutingUtils.redirect(resp, "/products");
            resp.sendRedirect("/products");
        }
    }
}

package net.ishop.servlets.ajax;

import net.ishop.entities.Product;
import net.ishop.models.constants.Constants;
import net.ishop.servlets.AbstractControllerServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ajax/html/more/products/*")
public class DisplayMoreProductsByCategoryControllerServlet extends AbstractControllerServlet {
    private static final int SUBSTRING_INDEX = "/ajax/html/more/products".length();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryUrl = req.getRequestURI().substring(SUBSTRING_INDEX);
        List<Product> categoryProductsList = this.getProductService().getByCategoryProductsList(categoryUrl, this.getCurrentPage(req), Constants.MAX_PRODUCTS_PER_HTML_PAGE);
        req.setAttribute("productsList", categoryProductsList);
//        RoutingUtils.forwardToFragment("products_list.jsp", req, resp);
        req.getRequestDispatcher("/WEB-INF/jsp/fragments/products_list.jsp").forward(req, resp);
    }
}

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

@WebServlet("/ajax/html/more/products")
public class DisplayMoreAllProductsControllerServlet extends AbstractControllerServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = this.getCurrentPage(req);
        List<Product> productsList
                = this.getProductService().getAllProductsList(currentPage, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
        req.setAttribute("productsList", productsList);

//        RoutingUtils.forwardToFragment("products_list.jsp", req, resp);
        req.getRequestDispatcher("/WEB-INF/jsp/fragments/products_list.jsp").forward(req, resp);
    }
}

package net.ishop.servlets.view_controllers;

import net.ishop.entities.Product;
import net.ishop.models.constants.Constants;
import net.ishop.servlets.AbstractControllerServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class AllProductsControllerServlet extends AbstractControllerServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // get Products list from data base
        List<Product> productsList = this.getProductService().getAllProductsList(1, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
        // adding to the response attribute products list
        req.setAttribute("productsList", productsList);
        int countAllProducts = this.getProductService().getCountAllProducts();
        int countPage = this.getCountPage(countAllProducts, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
        req.setAttribute("countPage", countPage);
        // adding to the response attribute of page (products.jsp) for dynamic loading
        // and forward to the template page (page-template.jsp)
//        RoutingUtils.forwardToPageTemplate("products.jsp", req, resp);
        req.setAttribute("dynamicLoadingPage","pages/products.jsp");
        req.getRequestDispatcher("/WEB-INF/jsp/page-template.jsp").forward(req, resp);
    }
}

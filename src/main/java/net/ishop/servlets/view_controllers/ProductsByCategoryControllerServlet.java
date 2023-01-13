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

@WebServlet("/products/*")
public class ProductsByCategoryControllerServlet extends AbstractControllerServlet {
    private static final int SUBSTRING_INDEX = "/products".length();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryUrl = req.getRequestURI().substring(SUBSTRING_INDEX);
        List<Product> categoryProductsList = this.getProductService()
                .getByCategoryProductsList(categoryUrl, 1, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
        req.setAttribute("productsList", categoryProductsList);
        int countByCategoryProducts = this.getProductService().getCountByCategoryProducts(categoryUrl);
        int countPage = this.getCountPage(countByCategoryProducts, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
        req.setAttribute("countPage", countPage);
        req.setAttribute("selectedCategoryUrl", categoryUrl);
    //        RoutingUtils.forwardToPageTemplate("products.jsp", req ,resp);
        req.setAttribute("dynamicLoadingPage","pages/products.jsp" );
        req.getRequestDispatcher("/WEB-INF/jsp/page-template.jsp").forward(req, resp);
    }
}

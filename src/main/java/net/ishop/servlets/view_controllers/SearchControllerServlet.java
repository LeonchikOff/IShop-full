package net.ishop.servlets.view_controllers;

import net.ishop.entities.Product;
import net.ishop.models.constants.Constants;
import net.ishop.models.forms.SearchForm;
import net.ishop.servlets.AbstractControllerServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class SearchControllerServlet extends AbstractControllerServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SearchForm searchForm = this.createSearchForm(req);
        req.setAttribute("searchForm", searchForm);

        List<Product> productsList =
                this.getProductService().getBySearchFormProductsList(searchForm, 1, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
        req.setAttribute("productsList", productsList);

        int countBySearchFormProducts = this.getProductService().getCountBySearchFormProducts(searchForm);
        req.setAttribute("productCount", countBySearchFormProducts);
        int countPage = this.getCountPage(countBySearchFormProducts, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
        req.setAttribute("countPage", countPage);


//        RoutingUtils.forwardToPageTemplate("search_result.jsp", req, resp);
        req.setAttribute("dynamicLoadingPage", "pages/search_result.jsp");
        req.getRequestDispatcher("/WEB-INF/jsp/page-template.jsp").forward(req, resp);
    }
}

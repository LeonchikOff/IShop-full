package net.ishop.servlets;

import net.ishop.models.forms.ProductForm;
import net.ishop.models.forms.SearchForm;
import net.ishop.services.OrderService;
import net.ishop.services.ProductService;
import net.ishop.services.SocialService;
import net.ishop.services.impl.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

//Abstract controller that combines the logic inherent in all its inheriting controllers
public abstract class AbstractControllerServlet extends HttpServlet {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private ProductService productService;
    private OrderService orderService;
    private SocialService socialService;


    @Override
    public void init() throws ServletException {
        productService = ServiceManager.getInstance(this.getServletContext()).getProductService();
        orderService = ServiceManager.getInstance(this.getServletContext()).getOrderService();
        socialService = ServiceManager.getInstance(this.getServletContext()).getSocialService();

    }

    public final ProductService getProductService() {
        return productService;
    }

    public final OrderService getOrderService() {
        return orderService;
    }

    public final SocialService getSocialService() {
        return socialService;
    }


    public final SearchForm createSearchForm(HttpServletRequest request) {
        String query = request.getParameter("query");
        String[] categories = request.getParameterValues("category");
        String[] producers = request.getParameterValues("producer");
        return new SearchForm(query, categories, producers);
    }

    public final ProductForm createProductForm(HttpServletRequest request) {
        int idProduct = Integer.parseInt(request.getParameter("idProduct"));
        int count = Integer.parseInt(request.getParameter("count"));
        return new ProductForm(idProduct, count);
    }

    public final int getCountPage(int countAllProduct, int countProductPerPage) {
        int countPage = countAllProduct / countProductPerPage;
        if (countPage * countProductPerPage != countAllProduct)
            countPage++;
        return countPage;
    }

    public final int getCurrentPage(HttpServletRequest request) {
        try {
            return Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException numberFormatException) {
            return 1;
        }
    }
}

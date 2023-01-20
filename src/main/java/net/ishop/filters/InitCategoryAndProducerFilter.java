package net.ishop.filters;

import net.ishop.models.constants.Constants;
import net.ishop.services.interfaces.ProductService;
import net.ishop.services.ServiceManager;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Example! dynamic loading attr from BD at the filter level (that is, with each request)
//@WebFilter
public class InitCategoryAndProducerFilter extends AbstractFilter {
    ProductService productService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        productService = ServiceManager.getInstance(filterConfig.getServletContext()).getProductService();
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setAttribute(Constants.CATEGORY_LIST_ATTR_NAME, productService.getAllCategoriesList());
        request.setAttribute(Constants.PRODUCER_LIST_ATTR_NAME, productService.getAllProducersList());
        chain.doFilter(request, response);
    }
}

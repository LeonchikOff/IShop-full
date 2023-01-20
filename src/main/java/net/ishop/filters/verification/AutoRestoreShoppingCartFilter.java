package net.ishop.filters.verification;

import net.ishop.filters.AbstractFilter;
import net.ishop.models.ShoppingCart;
import net.ishop.services.interfaces.OrderService;
import net.ishop.services.ServiceManager;
import net.ishop.utils.SessionShoppingCartUtils;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "AutoRestoreShoppingCartFilter")
public class AutoRestoreShoppingCartFilter extends AbstractFilter {
    private static final String SHOPPING_CARD_DESERIALIZATION_DONE = "SHOPPING_CARD_DESERIALIZATION_DONE";
    private OrderService orderService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        orderService = ServiceManager.getInstance(filterConfig.getServletContext()).getOrderService();
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(request.getSession().getAttribute(SHOPPING_CARD_DESERIALIZATION_DONE) == null) {
            if(!SessionShoppingCartUtils.isCurrentShoppingCartCreated(request)) {
                Cookie currentShoppingCartCookie = SessionShoppingCartUtils.findCurrentShoppingCartCookie(request);
                if(currentShoppingCartCookie != null) {
                    ShoppingCart deserializeShoppingCart = orderService.deserializeShoppingCart(currentShoppingCartCookie.getValue());
                    if(deserializeShoppingCart != null) {
                        SessionShoppingCartUtils.setCurrentShoppingCart(request, deserializeShoppingCart);
                    }
                }
            }
            request.getSession().setAttribute(SHOPPING_CARD_DESERIALIZATION_DONE, Boolean.TRUE);
        }
        chain.doFilter(request, response);
    }
}

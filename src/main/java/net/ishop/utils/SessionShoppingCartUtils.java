package net.ishop.utils;

import net.ishop.models.ShoppingCart;
import net.ishop.models.constants.Constants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class SessionShoppingCartUtils {

    public static ShoppingCart getCurrentShoppingCart(HttpServletRequest req) {
        return (ShoppingCart) req.getSession().getAttribute(Constants.CURRENT_SHOPPING_CART);
    }


    public static void setCurrentShoppingCart(HttpServletRequest request, ShoppingCart shoppingCart) {
        request.getSession().setAttribute(Constants.CURRENT_SHOPPING_CART, shoppingCart);
    }

    public static boolean isCurrentShoppingCartCreated(HttpServletRequest request) {
        return request.getSession().getAttribute(Constants.CURRENT_SHOPPING_CART) != null;
    }

    public static void clearCurrentShoppingCart(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(Constants.CURRENT_SHOPPING_CART);
        Cookie currentShoppingCartCookie = findCurrentShoppingCartCookie(request);
        if (currentShoppingCartCookie != null) {
            Cookie updatedCurrentCookie = new Cookie(currentShoppingCartCookie.getName(), null);
            updatedCurrentCookie.setMaxAge(0);
            response.addCookie(updatedCurrentCookie);
        }
    }

    public static Cookie findCurrentShoppingCartCookie(HttpServletRequest request) {
        return CookiesUtils.findCookieByName(request, Constants.CookieForShoppingCart.SHOPPING_CART_COOKIE.getName());
    }

    public static void updateCurrentShoppingCartCookie(HttpServletResponse response, String cookieValue) {
        Cookie cookie = new Cookie(Constants.CookieForShoppingCart.SHOPPING_CART_COOKIE.getName(), cookieValue);
        cookie.setMaxAge(Constants.CookieForShoppingCart.SHOPPING_CART_COOKIE.getTimeOfLife());
        response.addCookie(cookie);
    }
}

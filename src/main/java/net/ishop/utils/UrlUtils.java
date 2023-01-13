package net.ishop.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UrlUtils {
    private UrlUtils() {
    }

    public static boolean isAjaxUrl(String url) {
        return url.startsWith("/ajax/");
    }

    public static boolean isAjaxJsonUrl(String url) {
        return url.startsWith("/ajax/json/");
    }

    public static boolean isAjaxHtmlUrl(String url) {
        return url.startsWith("/ajax/html/");
    }

    public static boolean isStaticUrl(String url) {
        return url.startsWith("/static/");
    }

    public static boolean isMediaUrl(String url) {
        return url.startsWith("/media/");
    }

    public static String getCurrentRequestUrl(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        if (queryString != null) {
            requestURI = requestURI + "?" + queryString;
        }
        return requestURI;

//        String queryString = request.getQueryString();
//
//        if(queryString == null) {
//            return request.getRequestURI();
//        } else {
//            return request.getRequestURI() + "?" + queryString;
//        }
    }
}

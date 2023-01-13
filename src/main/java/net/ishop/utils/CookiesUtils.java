package net.ishop.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public abstract class CookiesUtils {
    public static Cookie findCookieByName(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(cookieName)) {
                    if(cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                        return cookie;
                    }
                }
            }
        }
        return null;
    }
}


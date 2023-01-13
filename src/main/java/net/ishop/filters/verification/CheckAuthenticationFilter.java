package net.ishop.filters.verification;

import net.ishop.filters.AbstractFilter;
import net.ishop.models.constants.Constants;
import net.ishop.utils.RoutingUtils;
import net.ishop.utils.SessionAccountUtils;
import net.ishop.utils.UrlUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CheckAuthenticationFilter")
public class CheckAuthenticationFilter extends AbstractFilter {
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(SessionAccountUtils.isCurrentAccountCreated(request)) {
            chain.doFilter(request, response);
        } else {
            String currentRequestUrl = UrlUtils.getCurrentRequestUrl(request, response);
            if(UrlUtils.isAjaxUrl(currentRequestUrl)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().println("401");
                RoutingUtils.sendHtmlFragment(response, "401");
            } else {
                request.getSession().setAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGN_IN, currentRequestUrl);
                RoutingUtils.redirect(response, "/sign_in");
            }
        }
    }
}

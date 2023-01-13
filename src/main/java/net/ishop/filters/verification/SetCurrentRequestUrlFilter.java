package net.ishop.filters.verification;

import net.ishop.filters.AbstractFilter;
import net.ishop.models.constants.Constants;
import net.ishop.utils.UrlUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "SetCurrentRequestUrlFilter")
public class SetCurrentRequestUrlFilter extends AbstractFilter {
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String currentRequestUrl = UrlUtils.getCurrentRequestUrl(request, response);
        String method = request.getMethod();
        System.out.println(method + ": " + currentRequestUrl);
        request.setAttribute(Constants.CURRENT_REQUEST_URL, currentRequestUrl);
        chain.doFilter(request, response);
    }
}

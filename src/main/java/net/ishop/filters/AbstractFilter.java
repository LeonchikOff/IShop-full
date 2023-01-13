package net.ishop.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ishop.utils.UrlUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Abstract filter that combines the logic inherent in all its inheriting filters
public abstract class AbstractFilter implements Filter {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public final void doFilter
            (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String requestURI = httpServletRequest.getRequestURI();
        if (UrlUtils.isMediaUrl(requestURI) || UrlUtils.isStaticUrl(requestURI)) {
            chain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            this.doFilter(httpServletRequest, httpServletResponse, chain);
        }
    }

    public abstract void doFilter
            (HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;


    @Override
    public void destroy() {
    }
}

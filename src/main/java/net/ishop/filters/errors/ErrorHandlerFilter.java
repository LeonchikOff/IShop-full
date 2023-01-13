package net.ishop.filters.errors;

import net.ishop.exceptions.*;
import net.ishop.filters.AbstractFilter;
import net.ishop.utils.RoutingUtils;
import net.ishop.utils.UrlUtils;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(filterName = "ErrorHandlerFilter")
public class ErrorHandlerFilter extends AbstractFilter {
    private static final String INTERNAL_ERROR = "INTERNAL_ERROR";

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, new ThrowExceptionInsteadOnSendErrorResponse(response));
        } catch (Throwable throwable) {
            String requestURI = request.getRequestURI();
            if (throwable instanceof ValidationException) {
                this.LOGGER.warn("Request is not valid: " + throwable.getMessage());
            } else {
                this.LOGGER.error("Request " + requestURI + " failed: " + throwable.getMessage(), throwable);
            }
            handelException(requestURI, throwable, request, response);
        }
    }

    private int getStatusCode(Throwable throwable, HttpServletResponse response) {
        if (throwable instanceof AbstractApplicationException) {
            return ((AbstractApplicationException) throwable).getCode();
        } else {
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
    }

    private void handelException(String requestURI, Throwable throwable, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int statusCode = getStatusCode(throwable, response);
        response.setStatus(statusCode);

        if (UrlUtils.isAjaxJsonUrl(requestURI)) {
            JSONObject json = new JSONObject();
            json.put("message", throwable instanceof ValidationException ? throwable.getMessage() : INTERNAL_ERROR);
            RoutingUtils.sendJSON(json, response);
//            response.setContentType("application/json");
//            PrintWriter responseWriter = response.getWriter();
//            responseWriter.println(json.toString());
//            responseWriter.close();
        }
        if (UrlUtils.isAjaxHtmlUrl(requestURI)) {
            RoutingUtils.sendHtmlFragment(response, INTERNAL_ERROR);
//            response.setContentType("text/html");
//            PrintWriter responseWriter = response.getWriter();
//            responseWriter.println(INTERNAL_ERROR);
//            responseWriter.close();
        } else {
            request.setAttribute("statusCode", statusCode);
            RoutingUtils.forwardToPageTemplate("error.jsp", request, response);
        }
    }

    private static class ThrowExceptionInsteadOnSendErrorResponse extends HttpServletResponseWrapper {
        public ThrowExceptionInsteadOnSendErrorResponse(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void sendError(int statusCode) throws IOException {
            sendError(statusCode, INTERNAL_ERROR);
        }

        @Override
        public void sendError(int statusCode, String msg) throws IOException {
            switch (statusCode) {
                case 403: {
                    throw new AccessDeniedException(msg);
                }
                case 404: {
                    throw new ResourceNotFoundException(msg);
                }
                case 400: {
                    throw new ValidationException(msg);
                }
                default: {
                    throw new InternalServerErrorException(msg);
                }
            }
        }
    }
}

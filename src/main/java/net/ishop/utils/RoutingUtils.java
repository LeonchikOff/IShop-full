package net.ishop.utils;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public final class RoutingUtils {
    public static void redirect(HttpServletResponse response, String urlLocationToRedirect)
            throws IOException {
        response.sendRedirect(urlLocationToRedirect);
    }

    public static void forwardToFragment(String jspFragment, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/fragments/" + jspFragment).forward(request, response);
    }

    public static void forwardToPageTemplate(String dynamicLoadingPageJsp, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("dynamicLoadingPage", "pages/" + dynamicLoadingPageJsp);
        request.getRequestDispatcher("/WEB-INF/jsp/page-template.jsp").forward(request, response);
    }

    public static void sendHtmlFragment(HttpServletResponse response, String textHtml)
            throws IOException {
        response.setContentType("text/html");
        PrintWriter responseWriter = response.getWriter();
        responseWriter.println(textHtml);
        responseWriter.close();
    }
    public static void sendJSON(JSONObject jsonObject, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter responseWriter = response.getWriter();
        responseWriter.println(jsonObject.toString());
        responseWriter.close();

    }
}

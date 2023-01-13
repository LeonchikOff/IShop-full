package net.ishop.servlets.error;

import net.ishop.servlets.AbstractControllerServlet;
import net.ishop.utils.RoutingUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/error")
public class ErrorControllerServlet extends AbstractControllerServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        RoutingUtils.forwardToPageTemplate("error.jsp", req, resp);

//        req.setAttribute("dynamicLoadingPage","pages/error.jsp" );
//        req.getRequestDispatcher("/WEB-INF/jsp/page-template.jsp").forward(req, resp);
    }
}

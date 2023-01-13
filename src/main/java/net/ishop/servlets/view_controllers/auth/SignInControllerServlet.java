package net.ishop.servlets.view_controllers.auth;

import net.ishop.models.constants.Constants;
import net.ishop.servlets.AbstractControllerServlet;
import net.ishop.utils.RoutingUtils;
import net.ishop.utils.SessionAccountUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/sign_in")
public class SignInControllerServlet extends AbstractControllerServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionAccountUtils.isCurrentAccountCreated(req)) {
            RoutingUtils.redirect(resp, "/my_orders");
        } else {
//            RoutingUtils.forwardToPageTemplate("sign_in.jsp", req, resp);
            req.setAttribute("dynamicLoadingPage", "pages/sign_in.jsp");
            req.getRequestDispatcher("/WEB-INF/jsp/page-template.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionAccountUtils.isCurrentAccountCreated(req)) {
            RoutingUtils.redirect(resp, "/my_orders");
        } else {
            String targetUrl = req.getParameter("target");
            if (targetUrl != null) {
                req.getSession().setAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGN_IN, targetUrl);
            }
            RoutingUtils.redirect(resp, this.getSocialService().getAuthorizeUrl());
        }
    }
}

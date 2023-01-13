package net.ishop.servlets.view_controllers.auth;

import net.ishop.models.constants.Constants;
import net.ishop.models.social.CurrentAccount;
import net.ishop.models.social.SocialAccount;
import net.ishop.servlets.AbstractControllerServlet;
import net.ishop.utils.RoutingUtils;
import net.ishop.utils.SessionAccountUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@WebServlet("/from-social")
public class FromSocialControllerServlet extends AbstractControllerServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        if (code != null) {
            SocialAccount socialAccount = this.getSocialService().getSocialAccount(code);;
            CurrentAccount currentAccount = this.getOrderService().authenticateViaDB(socialAccount);
            SessionAccountUtils.setCurrentAccount(req, currentAccount);
            redirectToSuccessPage(req, resp);
        } else {
            LOGGER.warn("Parameter code not found");
            if (req.getSession().getAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGN_IN) != null) {
                req.getSession().removeAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGN_IN);
            }
            RoutingUtils.redirect(resp, "/sign_in");
        }
    }

    protected void redirectToSuccessPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String targetUrl = (String) request.getSession().getAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGN_IN);
        if (targetUrl != null) {
            request.getSession().removeAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGN_IN);
            RoutingUtils.redirect(response, URLDecoder.decode(targetUrl, "UTF-8"));
        } else {
            RoutingUtils.redirect(response, "/my_orders");
        }
    }
}

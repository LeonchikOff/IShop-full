package net.ishop.servlets.view_controllers.auth;

import net.ishop.servlets.AbstractControllerServlet;
import net.ishop.utils.RoutingUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/sign_out")
public class SignOutControllerServlet extends AbstractControllerServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        RoutingUtils.redirect(resp, "/products");
    }
}

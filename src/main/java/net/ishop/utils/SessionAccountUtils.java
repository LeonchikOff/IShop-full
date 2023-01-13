package net.ishop.utils;

import net.ishop.models.constants.Constants;
import net.ishop.models.social.CurrentAccount;

import javax.servlet.http.HttpServletRequest;

public class SessionAccountUtils {
    public static void setCurrentAccount(HttpServletRequest request, CurrentAccount currentAccount) {
        request.getSession().setAttribute(Constants.CURRENT_ACCOUNT, currentAccount);
    }

    public static CurrentAccount getCurrentAccount(HttpServletRequest request) {
       return (CurrentAccount) request.getSession().getAttribute(Constants.CURRENT_ACCOUNT);
    }

    public static boolean isCurrentAccountCreated(HttpServletRequest request) {
        return getCurrentAccount(request) != null;
    }
}

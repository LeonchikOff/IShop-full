package net.ishop.exceptions;

import javax.servlet.http.HttpServletResponse;

public class AccessDeniedException extends AbstractApplicationException{
    public AccessDeniedException(String s) {
        super(s, HttpServletResponse.SC_FORBIDDEN);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause, HttpServletResponse.SC_FORBIDDEN);
    }

    public AccessDeniedException(Throwable cause) {
        super(cause, HttpServletResponse.SC_FORBIDDEN);
    }
}

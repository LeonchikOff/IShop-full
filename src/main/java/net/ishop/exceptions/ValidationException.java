package net.ishop.exceptions;

import javax.servlet.http.HttpServletResponse;

public class ValidationException extends AbstractApplicationException {
    public ValidationException(String s) {
        super(s, HttpServletResponse.SC_BAD_REQUEST);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause, HttpServletResponse.SC_BAD_REQUEST);
    }

    public ValidationException(Throwable cause) {
        super(cause, HttpServletResponse.SC_BAD_REQUEST);
    }
}

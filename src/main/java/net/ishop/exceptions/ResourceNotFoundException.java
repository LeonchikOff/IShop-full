package net.ishop.exceptions;

import javax.servlet.http.HttpServletResponse;

public class ResourceNotFoundException extends AbstractApplicationException  {
    public ResourceNotFoundException(String s) {
        super(s, HttpServletResponse.SC_NOT_FOUND);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause, HttpServletResponse.SC_NOT_FOUND);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause, HttpServletResponse.SC_NOT_FOUND);
    }
}

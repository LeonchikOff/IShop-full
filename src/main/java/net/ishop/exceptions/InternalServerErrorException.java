package net.ishop.exceptions;

import javax.servlet.http.HttpServletResponse;

public class InternalServerErrorException extends AbstractApplicationException {

    public InternalServerErrorException(String s) {
        super(s, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorException(Throwable cause) {
        super(cause, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}

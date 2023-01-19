package net.framework.exception;


public class FrameworkSystemException extends RuntimeException {
    public FrameworkSystemException(String s) {
    }

    public FrameworkSystemException(Throwable cause) {
        super(cause);
    }

    public FrameworkSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrameworkSystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}


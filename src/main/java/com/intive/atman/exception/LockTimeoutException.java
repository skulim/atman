package com.intive.atman.exception;

public class LockTimeoutException extends AccountOperationException {
    private static final long serialVersionUID = 8122495747353972750L;

    public LockTimeoutException() {
    }

    public LockTimeoutException(String message) {
        super(message);
    }

    public LockTimeoutException(Throwable cause) {
        super(cause);
    }

    public LockTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

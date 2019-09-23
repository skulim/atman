package com.intive.atman.exception;

public class AccountOperationException extends Exception {

    private static final long serialVersionUID = -8560307426500069313L;

    public AccountOperationException() {
    }

    public AccountOperationException(String message) {
        super(message);
    }

    public AccountOperationException(Throwable cause) {
        super(cause);
    }

    public AccountOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

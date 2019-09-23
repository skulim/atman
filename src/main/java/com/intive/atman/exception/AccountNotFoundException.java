package com.intive.atman.exception;

public class AccountNotFoundException extends AccountOperationException {

    private static final long serialVersionUID = 3422040302066915359L;

    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(Throwable cause) {
        super(cause);
    }

}

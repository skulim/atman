package com.intive.atman.exception;

public class TransactionOperationException extends Exception {

    private static final long serialVersionUID = -8560307426500069313L;

    public TransactionOperationException() {
    }

    public TransactionOperationException(String message) {
        super(message);
    }

    public TransactionOperationException(Throwable cause) {
        super(cause);
    }

    public TransactionOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

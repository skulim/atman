package com.intive.atman.exception;

public class LackOfFundsException extends AccountOperationException {

    private static final long serialVersionUID = 5831287302644403150L;

    public LackOfFundsException() {
    }

    public LackOfFundsException(String message) {
        super(message);
    }

    public LackOfFundsException(Throwable cause) {
        super(cause);
    }

    public LackOfFundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public LackOfFundsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

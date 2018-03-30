package com.mt940.daemon.exceptions;

public abstract class MT940Exception extends Exception {

    protected MT940Exception() {
        //default constructor
    }

    protected MT940Exception(String message) {
        super(message);
    }

    protected MT940Exception(String message, Throwable cause) {
        super(message, cause);
    }

    protected MT940Exception(Throwable cause) {
        super(cause);
    }

}

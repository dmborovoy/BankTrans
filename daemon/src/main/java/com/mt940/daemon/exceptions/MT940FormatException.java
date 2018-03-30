package com.mt940.daemon.exceptions;

public class MT940FormatException extends MT940Exception {

    public MT940FormatException() {
        //default constructor
    }

    public MT940FormatException(String message) {
        super(message);
    }

    public MT940FormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public MT940FormatException(Throwable cause) {
        super(cause);
    }

}

package com.mt940.daemon.exceptions;

public class MT940MandatoryFieldException extends MT940Exception {

    public MT940MandatoryFieldException() {
        //default constructor
    }

    public MT940MandatoryFieldException(String message) {
        super(message);
    }

    public MT940MandatoryFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public MT940MandatoryFieldException(Throwable cause) {
        super(cause);
    }

}

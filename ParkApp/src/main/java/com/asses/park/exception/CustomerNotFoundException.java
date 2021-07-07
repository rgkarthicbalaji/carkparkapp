package com.asses.park.exception;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException() {
        super();
    }

    public CustomerNotFoundException(final String message) {
        super(message);
    }

    public CustomerNotFoundException(final Throwable cause) {
        super(cause);
    }

    public CustomerNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

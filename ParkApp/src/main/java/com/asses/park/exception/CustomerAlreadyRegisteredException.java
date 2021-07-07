package com.asses.park.exception;

public class CustomerAlreadyRegisteredException extends Exception{

    public CustomerAlreadyRegisteredException() {
        super();
    }

    public CustomerAlreadyRegisteredException(final String message) {
        super(message);
    }

    public CustomerAlreadyRegisteredException(final Throwable cause) {
        super(cause);
    }

    public CustomerAlreadyRegisteredException(final String message, final Throwable cause) {
        super(message,cause);
    }

}

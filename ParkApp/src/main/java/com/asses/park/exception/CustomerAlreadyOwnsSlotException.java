package com.asses.park.exception;

public class CustomerAlreadyOwnsSlotException extends Exception{

    public CustomerAlreadyOwnsSlotException() {
        super();
    }

    public CustomerAlreadyOwnsSlotException(final String message) {
        super(message);
    }

    public CustomerAlreadyOwnsSlotException(final Throwable cause) {
        super(cause);
    }

    public CustomerAlreadyOwnsSlotException(final String message, final Throwable cause) {
        super(message,cause);
    }

}

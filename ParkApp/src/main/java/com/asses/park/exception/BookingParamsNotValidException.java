package com.asses.park.exception;

public class BookingParamsNotValidException extends Exception{

    public BookingParamsNotValidException() {
        super();
    }

    public BookingParamsNotValidException(final String message) {
        super(message);
    }

    public BookingParamsNotValidException(final Throwable cause) {
        super(cause);
    }

    public BookingParamsNotValidException(final String message, final Throwable cause) {
        super(message,cause);
    }

}

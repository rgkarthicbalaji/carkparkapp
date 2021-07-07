package com.asses.park.exception;

public class ParkingSlotTimeNotInRageException extends Exception{

    public ParkingSlotTimeNotInRageException() {
        super();
    }

    public ParkingSlotTimeNotInRageException(final String message) {
        super(message);
    }

    public ParkingSlotTimeNotInRageException(final Throwable cause) {
        super(cause);
    }

    public ParkingSlotTimeNotInRageException(final String message, final Throwable cause) {
        super(message,cause);
    }

}

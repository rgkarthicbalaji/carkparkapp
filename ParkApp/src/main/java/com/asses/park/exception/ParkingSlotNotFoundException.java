package com.asses.park.exception;

public class ParkingSlotNotFoundException  extends Exception{

    public ParkingSlotNotFoundException() {
        super();
    }

    public ParkingSlotNotFoundException(final String message) {
        super(message);
    }

    public ParkingSlotNotFoundException(final Throwable cause) {
        super(cause);
    }

    public ParkingSlotNotFoundException(final String message, final Throwable cause) {
        super(message,cause);
    }

}

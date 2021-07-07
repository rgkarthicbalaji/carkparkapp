package com.asses.park.exception;

public class ParkingSlotAlreadyBookedException  extends Exception{

    public ParkingSlotAlreadyBookedException() {
        super();
    }

    public ParkingSlotAlreadyBookedException(final String message) {
        super(message);
    }

    public ParkingSlotAlreadyBookedException(final Throwable cause) {
        super(cause);
    }

    public ParkingSlotAlreadyBookedException(final String message, final Throwable cause) {
        super(message,cause);
    }

}

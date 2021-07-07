package com.asses.park.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class ExceptionHandlers extends ResponseEntityExceptionHandler {

    private static final String CUSTOMER_ALREADY_REGISTERED = "Customer Already Registered";
    private static final String CUSTOMER_NOT_FOUND = "Customer Not Found";
    private static final String CUSTOMER_ALREADY_OWNS_SLOT = "Customer Already Owns A Booked Slot";
    private static final String PARKING_SLOT_TIME_NOT_INRANGE = "ParkingHour Slot Time Does Not Fall Within Agreeable Booking Range";
    private static final String PARKING_SLOT_NOT_FOUND = "ParkingSlot Not Found";
    private static final String PARKING_SLOT_ALREADY_BOOKED = "Parking Slot Already Booked";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(
                new ResponseError(BAD_REQUEST, ex.getBindingResult().getFieldError().getDefaultMessage())
        );
    }

    @ExceptionHandler(CustomerAlreadyRegisteredException.class)
    public ResponseEntity<Object> handleCustomerAlreadyRegistered(CustomerAlreadyRegisteredException ex) {
        log.error(CUSTOMER_ALREADY_REGISTERED);

        return buildResponseEntity(
                new ResponseError(FORBIDDEN, CUSTOMER_ALREADY_REGISTERED)
        );
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> handleCustomerNotFound(CustomerNotFoundException ex) {
        log.error(CUSTOMER_NOT_FOUND);

        return buildResponseEntity(
                new ResponseError(NOT_FOUND, CUSTOMER_NOT_FOUND)
        );
    }

    @ExceptionHandler(ParkingSlotTimeNotInRageException.class)
    public ResponseEntity<Object> handleParkingSlotTimeNotInRage(ParkingSlotTimeNotInRageException ex) {
        log.error(PARKING_SLOT_TIME_NOT_INRANGE);

        return buildResponseEntity(
                new ResponseError(BAD_REQUEST, PARKING_SLOT_TIME_NOT_INRANGE)
        );
    }

    @ExceptionHandler(ParkingSlotNotFoundException.class)
    public ResponseEntity<Object> handleParkingSlotNotFound(ParkingSlotNotFoundException ex) {
        log.error(PARKING_SLOT_NOT_FOUND);

        return buildResponseEntity(
                new ResponseError(NOT_FOUND, PARKING_SLOT_NOT_FOUND)
        );
    }

    @ExceptionHandler(ParkingSlotAlreadyBookedException.class)
    public ResponseEntity<Object> handleParkingSlotAlreadyBooked(ParkingSlotAlreadyBookedException ex) {
        log.error(PARKING_SLOT_ALREADY_BOOKED);

        return buildResponseEntity(
                new ResponseError(FORBIDDEN, PARKING_SLOT_ALREADY_BOOKED)
        );
    }

    @ExceptionHandler(CustomerAlreadyOwnsSlotException.class)
    public ResponseEntity<Object> handleCustomerAlreadyOwnsSlot(ParkingSlotAlreadyBookedException ex) {
        log.error(CUSTOMER_ALREADY_OWNS_SLOT);

        return buildResponseEntity(
                new ResponseError(FORBIDDEN, CUSTOMER_ALREADY_OWNS_SLOT)
        );
    }

    @ExceptionHandler(BookingParamsNotValidException.class)
    public ResponseEntity<Object> handleInvalidBookingParameters(BookingParamsNotValidException ex) {
        log.error(ex.getMessage());

        return buildResponseEntity(
                new ResponseError(FORBIDDEN, ex.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {

        return buildResponseEntity(
                new ResponseError(INTERNAL_SERVER_ERROR, exception.getMessage())
        );
    }

    private ResponseEntity<Object> buildResponseEntity(ResponseError responseError) {
        return new ResponseEntity<>(responseError, responseError.getStatus());
    }
}
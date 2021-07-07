package com.asses.park.exception;

import com.asses.park.dto.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
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
                new CustomResponse(BAD_REQUEST, ex.getBindingResult().getFieldError().getDefaultMessage())
        );
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(
                new CustomResponse(BAD_REQUEST, ex.getRootCause().getMessage())
        );
    }

    @ExceptionHandler(CustomerAlreadyRegisteredException.class)
    public ResponseEntity<Object> handleCustomerAlreadyRegistered(CustomerAlreadyRegisteredException ex) {
        log.error(CUSTOMER_ALREADY_REGISTERED);

        return buildResponseEntity(
                new CustomResponse(FORBIDDEN, CUSTOMER_ALREADY_REGISTERED)
        );
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> handleCustomerNotFound(CustomerNotFoundException ex) {
        log.error(CUSTOMER_NOT_FOUND);

        return buildResponseEntity(
                new CustomResponse(NOT_FOUND, CUSTOMER_NOT_FOUND)
        );
    }

    @ExceptionHandler(ParkingSlotTimeNotInRageException.class)
    public ResponseEntity<Object> handleParkingSlotTimeNotInRage(ParkingSlotTimeNotInRageException ex) {
        log.error(PARKING_SLOT_TIME_NOT_INRANGE);

        return buildResponseEntity(
                new CustomResponse(BAD_REQUEST, PARKING_SLOT_TIME_NOT_INRANGE)
        );
    }

    @ExceptionHandler(ParkingSlotNotFoundException.class)
    public ResponseEntity<Object> handleParkingSlotNotFound(ParkingSlotNotFoundException ex) {
        log.error(PARKING_SLOT_NOT_FOUND);

        return buildResponseEntity(
                new CustomResponse(NOT_FOUND, PARKING_SLOT_NOT_FOUND)
        );
    }

    @ExceptionHandler(ParkingSlotAlreadyBookedException.class)
    public ResponseEntity<Object> handleParkingSlotAlreadyBooked(ParkingSlotAlreadyBookedException ex) {
        log.error(PARKING_SLOT_ALREADY_BOOKED);

        return buildResponseEntity(
                new CustomResponse(FORBIDDEN, PARKING_SLOT_ALREADY_BOOKED)
        );
    }

    @ExceptionHandler(CustomerAlreadyOwnsSlotException.class)
    public ResponseEntity<Object> handleCustomerAlreadyOwnsSlot(CustomerAlreadyOwnsSlotException ex) {
        log.error(CUSTOMER_ALREADY_OWNS_SLOT);

        return buildResponseEntity(
                new CustomResponse(FORBIDDEN, CUSTOMER_ALREADY_OWNS_SLOT)
        );
    }

    @ExceptionHandler(BookingParamsNotValidException.class)
    public ResponseEntity<Object> handleInvalidBookingParameters(BookingParamsNotValidException ex) {
        log.error(ex.getMessage());

        return buildResponseEntity(
                new CustomResponse(FORBIDDEN, ex.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {

        return buildResponseEntity(
                new CustomResponse(INTERNAL_SERVER_ERROR, exception.getMessage())
        );
    }

    private ResponseEntity<Object> buildResponseEntity(CustomResponse customResponse) {
        return new ResponseEntity<>(customResponse, customResponse.getStatus());
    }
}
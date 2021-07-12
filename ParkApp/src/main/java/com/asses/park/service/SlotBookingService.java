package com.asses.park.service;

import com.asses.park.exception.*;
import com.asses.park.model.Customer;
import com.asses.park.model.ParkingSlot;
import com.asses.park.model.SlotBooking;
import com.asses.park.repository.CustomerRepository;
import com.asses.park.repository.ParkingSlotRepository;
import com.asses.park.repository.SlotBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SlotBookingService {

    @Autowired
    SlotBookingRepository slotBookingRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ParkingSlotRepository parkingSlotRepository;

    private final String BOOKING_UNIQUEID_INAPPROPRIATE = "Provided bookingUniqueId Is Inappropriate For Booking";
    private final String BOOKING_UNIQUEID_INVALID_REFERENCE = "Provided bookingUniqueId Is Invalid Or Would Have Elapsed";
    private final String BOOKING_END_TIME_NOT_ELASPED = "Booking End Time Is yet To Get Elaspsed,Perform Reallocation Later";

    @Transactional
    public SlotBooking allocateParkingSlot(SlotBooking slotBooking) throws Exception {
        SlotBooking bookingResp = new SlotBooking();
        Optional<Customer> customerOptional = customerRepository.findById(slotBooking.getCustomer().getSsNumber());
        if (customerOptional.isPresent()) {
            if (slotBooking.getHourSlot() >= 1 && slotBooking.getHourSlot() <= 4) {
                Optional<ParkingSlot> parkingSlotPresentOpt = parkingSlotRepository.findById(slotBooking.getParkingSlot().getSlotId());
                if (!parkingSlotPresentOpt.isPresent()) {
                    throw new ParkingSlotNotFoundException();
                }
                Optional<ParkingSlot> parkingSlotOpt = Optional.ofNullable(parkingSlotRepository.findBySlotIdAndIsBooked(slotBooking.getParkingSlot().getSlotId(), Boolean.TRUE));
                if (parkingSlotOpt.isPresent()) {
                    throw new ParkingSlotAlreadyBookedException();
                }

                Customer customer = customerOptional.get();
                Optional<List<SlotBooking>> optionalSlotBookingList = Optional.ofNullable(slotBookingRepository.findByCustomerAndIsBookedNow(customer, Boolean.TRUE));
                if (optionalSlotBookingList.isPresent()) {
                    List<SlotBooking> slotBookings = optionalSlotBookingList.get();
                    if (slotBookings.size() > 0) {
                        throw new CustomerAlreadyOwnsSlotException();
                    }
                }

                Optional<ParkingSlot> parkingSltOpt = parkingSlotRepository.findById(slotBooking.getParkingSlot().getSlotId());

                slotBooking.setBookingUniqueId(UUID.randomUUID());
                slotBooking.setIsBookedNow(Boolean.TRUE);
                ParkingSlot parkingSlot = parkingSltOpt.get();
                parkingSlot.setIsBooked(Boolean.TRUE);
                slotBooking.setParkingSlot(parkingSlot);
                slotBooking.setCustomer(customer);
                bookingResp = slotBookingRepository.save(slotBooking);

            } else {
                throw new ParkingSlotTimeNotInRageException();
            }
        } else {
            throw new CustomerNotFoundException();
        }
        return bookingResp;
    }

    @Transactional
    public SlotBooking reAllocateParkingSlot(SlotBooking slotBooking) throws Exception {
        SlotBooking bookingResp = new SlotBooking();
        Optional<Customer> customerOptional = customerRepository.findById(slotBooking.getCustomer().getSsNumber());
        if (customerOptional.isPresent()) {
            if (slotBooking.getHourSlot() >= 1 && slotBooking.getHourSlot() <= 4) {
                Optional<ParkingSlot> parkingSlotPresentOpt = parkingSlotRepository.findById(slotBooking.getParkingSlot().getSlotId());
                if (!parkingSlotPresentOpt.isPresent()) {
                    throw new ParkingSlotNotFoundException();
                }
                Optional<ParkingSlot> parkingSlotOpt = Optional.ofNullable(parkingSlotRepository.findBySlotIdAndIsBooked(slotBooking.getParkingSlot().getSlotId(), Boolean.TRUE));
                if (parkingSlotOpt.isPresent()) {
                    throw new ParkingSlotAlreadyBookedException();
                }
                Optional<List<SlotBooking>> slotBookingOptional = Optional.ofNullable(slotBookingRepository.findByBookingUniqueId(slotBooking.getBookingUniqueId()));
                if (slotBookingOptional.isPresent()) {
                    Optional<List<SlotBooking>> slotBookinUniqueOptional = Optional.ofNullable(slotBookingRepository.findByBookingUniqueIdAndIsBookedNow(slotBooking.getBookingUniqueId(), Boolean.TRUE));
                    if (slotBookinUniqueOptional.isPresent()) {
                        List<SlotBooking> slotBookings = slotBookinUniqueOptional.get();
                        if (slotBookings.size() == 1) {
                            SlotBooking presentSlotBooking = slotBookings.get(0);
                            if(Timestamp.valueOf(LocalDateTime.now()).before(presentSlotBooking.getEndTime())){
                                throw new BookingParamsNotValidException(BOOKING_END_TIME_NOT_ELASPED);
                            }else{
                            ParkingSlot presentParkingSlot = presentSlotBooking.getParkingSlot();
                            Customer presentCustomer = presentSlotBooking.getCustomer();

                            SlotBooking newSlotBooking = new SlotBooking();
                            newSlotBooking.setIsBookedNow(Boolean.TRUE);
                            newSlotBooking.setBookingUniqueId(slotBooking.getBookingUniqueId());
                            newSlotBooking.setStartTime(slotBooking.getStartTime());
                            newSlotBooking.setEndTime(slotBooking.getEndTime());
                            newSlotBooking.setHourSlot(slotBooking.getHourSlot());

                            Optional<ParkingSlot> parkingSltOpt = parkingSlotRepository.findById(slotBooking.getParkingSlot().getSlotId());
                            ParkingSlot newParkingSlot = parkingSltOpt.get();
                            newParkingSlot.setIsBooked(Boolean.TRUE);

                            newSlotBooking.setParkingSlot(newParkingSlot);
                            newSlotBooking.setCustomer(presentCustomer);

                            presentSlotBooking.setIsBookedNow(Boolean.FALSE);
                            presentParkingSlot.setIsBooked(Boolean.FALSE);
                            slotBookingRepository.saveAndFlush(presentSlotBooking);
                            bookingResp = slotBookingRepository.saveAndFlush(newSlotBooking);
                            }
                        } else {
                            throw new BookingParamsNotValidException(BOOKING_UNIQUEID_INAPPROPRIATE);
                        }
                    } else {
                        throw new BookingParamsNotValidException(BOOKING_UNIQUEID_INVALID_REFERENCE);
                    }
                } else {
                    throw new BookingParamsNotValidException(BOOKING_UNIQUEID_INVALID_REFERENCE);
                }
            } else {
                throw new ParkingSlotTimeNotInRageException();
            }
        } else {
            throw new CustomerNotFoundException();
        }
        return bookingResp;
    }

    public Long cancelParkingSlot(UUID bookingUniqueId) throws Exception {
        Long slotUsageHours = null;
        Optional<List<SlotBooking>> slotBookingOptional = Optional.ofNullable(slotBookingRepository.findByBookingUniqueId(bookingUniqueId)).filter(list->!list.isEmpty());
        if (slotBookingOptional.isPresent()) {
            Optional<List<SlotBooking>> slotBookinUniqueOptional = Optional.ofNullable(slotBookingRepository.findByBookingUniqueIdAndIsBookedNow(bookingUniqueId, Boolean.TRUE)).filter(list->!list.isEmpty());
            if (slotBookinUniqueOptional.isPresent()) {
                List<SlotBooking> slotBookings = slotBookinUniqueOptional.get();
                if (slotBookings.size() == 1) {
                    SlotBooking presentSlotBooking = slotBookings.get(0);

                    ParkingSlot presentParkingSlot = presentSlotBooking.getParkingSlot();

                    SlotBooking firstSlotBooking = slotBookingOptional.get().get(0);
                    LocalDateTime startUsageDateTime = firstSlotBooking.getStartTime().toLocalDateTime();
                    LocalDateTime endUsageDateTime = presentSlotBooking.getEndTime().toLocalDateTime();
                    Duration duration = Duration.between(startUsageDateTime, endUsageDateTime);

                    presentSlotBooking.setIsBookedNow(Boolean.FALSE);
                    presentParkingSlot.setIsBooked(Boolean.FALSE);
                    slotBookingRepository.saveAndFlush(presentSlotBooking);
                    slotUsageHours = duration.toHours();
                } else {
                    throw new BookingParamsNotValidException(BOOKING_UNIQUEID_INAPPROPRIATE);
                }
            } else {
                throw new BookingParamsNotValidException(BOOKING_UNIQUEID_INVALID_REFERENCE);
            }
        } else {
            throw new BookingParamsNotValidException(BOOKING_UNIQUEID_INVALID_REFERENCE);
        }
        return slotUsageHours;
    }

    @Scheduled(initialDelay = 16200000, fixedRate = 16200000)
    @Transactional
    public void runParkingSlotReallocationTask() throws Exception {
        Optional<List<SlotBooking>> slotBookingsOptional = Optional.ofNullable(slotBookingRepository.findByIsBookedNow(Boolean.TRUE));
        if (slotBookingsOptional.isPresent()) {
            List<SlotBooking> bookedSlotBookings = slotBookingsOptional.get();

            for (SlotBooking slotBooking : bookedSlotBookings) {
                Optional<Customer> customerOptional = customerRepository.findById(slotBooking.getCustomer().getSsNumber());
                if (customerOptional.isPresent()) {
                    if (slotBooking.getHourSlot() >= 1 && slotBooking.getHourSlot() <= 4) {
                        Optional<ParkingSlot> parkingSlotPresentOpt = parkingSlotRepository.findById(slotBooking.getParkingSlot().getSlotId());
                        if (!parkingSlotPresentOpt.isPresent()) {
                            throw new ParkingSlotNotFoundException();
                        }

                        Optional<List<SlotBooking>> slotBookingOptional = Optional.ofNullable(slotBookingRepository.findByBookingUniqueId(slotBooking.getBookingUniqueId()));
                        if (slotBookingOptional.isPresent()) {
                            Optional<List<SlotBooking>> slotBookinUniqueOptional = Optional.ofNullable(slotBookingRepository.findByBookingUniqueIdAndIsBookedNow(slotBooking.getBookingUniqueId(), Boolean.TRUE));
                            if (slotBookinUniqueOptional.isPresent()) {
                                List<SlotBooking> slotBookings = slotBookinUniqueOptional.get();
                                if (slotBookings.size() == 1) {
                                    SlotBooking presentSlotBooking = slotBookings.get(0);
                                    if (Timestamp.valueOf(LocalDateTime.now()).before(presentSlotBooking.getEndTime())) {
                                        System.out.println(BOOKING_END_TIME_NOT_ELASPED+" For: "+slotBooking.getBookingUniqueId());
                                    } else {
                                        ParkingSlot presentParkingSlot = presentSlotBooking.getParkingSlot();
                                        Customer presentCustomer = presentSlotBooking.getCustomer();

                                        SlotBooking newSlotBooking = new SlotBooking();
                                        newSlotBooking.setIsBookedNow(Boolean.TRUE);
                                        newSlotBooking.setBookingUniqueId(slotBooking.getBookingUniqueId());
                                        newSlotBooking.setStartTime(slotBooking.getStartTime());
                                        newSlotBooking.setEndTime(slotBooking.getEndTime());
                                        newSlotBooking.setHourSlot(slotBooking.getHourSlot());

                                        Optional<List<ParkingSlot>> parkingSltOpt = Optional.ofNullable(parkingSlotRepository.findByIsBooked(Boolean.FALSE));
                                        if (parkingSltOpt.isPresent()) {
                                            ParkingSlot newParkingSlot = parkingSltOpt.get().get(0);
                                            newParkingSlot.setIsBooked(Boolean.TRUE);

                                            newSlotBooking.setParkingSlot(newParkingSlot);
                                            newSlotBooking.setCustomer(presentCustomer);

                                            presentSlotBooking.setIsBookedNow(Boolean.FALSE);
                                            presentParkingSlot.setIsBooked(Boolean.FALSE);
                                            slotBookingRepository.saveAndFlush(presentSlotBooking);
                                            slotBookingRepository.saveAndFlush(newSlotBooking);
                                            System.out.println("Reallocation Done For: "+slotBooking.getBookingUniqueId());
                                        }
                                    }
                                } else {
                                    throw new BookingParamsNotValidException(BOOKING_UNIQUEID_INAPPROPRIATE);
                                }
                            } else {
                                throw new BookingParamsNotValidException(BOOKING_UNIQUEID_INVALID_REFERENCE);
                            }
                        } else {
                            throw new BookingParamsNotValidException(BOOKING_UNIQUEID_INVALID_REFERENCE);
                        }
                    } else {
                        throw new ParkingSlotTimeNotInRageException();
                    }
                } else {
                    throw new CustomerNotFoundException();
                }
            }
        }
    }
}
package com.asses.park.service;

import com.asses.park.model.Customer;
import com.asses.park.model.ParkingSlot;
import com.asses.park.model.SlotBooking;
import com.asses.park.repository.CustomerRepository;
import com.asses.park.repository.ParkingSlotRepository;
import com.asses.park.repository.SlotBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SlotBookingService {

    @Autowired
    SlotBookingRepository slotBookingRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ParkingSlotRepository parkingSlotRepository;

    //TODO: Handle error if user already exist
    @Transactional
    public SlotBooking allocateParkingSlot(SlotBooking slotBooking) throws Exception {
        SlotBooking bookingResp = new SlotBooking();
        Optional<Customer> customerOptional = customerRepository.findById(slotBooking.getCustomer().getSsNumber());
        if (customerOptional.isPresent()) {
            if (slotBooking.getHourSlot() >= 1 && slotBooking.getHourSlot() <= 4) {
                Optional<ParkingSlot> parkingSlotPresentOpt = parkingSlotRepository.findById(slotBooking.getParkingSlot().getSlotId());
                if (!parkingSlotPresentOpt.isPresent()) {
                    throw new Exception("ParkingSlot Not Found Exception");
                }
                Optional<ParkingSlot> parkingSlotOpt = Optional.ofNullable(parkingSlotRepository.findBySlotIdAndIsBooked(slotBooking.getParkingSlot().getSlotId(), Boolean.TRUE));
                if (parkingSlotOpt.isPresent()) {
                    throw new Exception("ParkingSlot Already Booked Exception");
                }

                Customer customer = customerOptional.get();
                Optional<List<SlotBooking>> optionalSlotBookingList = Optional.ofNullable(slotBookingRepository.findByCustomerAndIsBookedNow(customer, Boolean.TRUE));
                if (optionalSlotBookingList.isPresent()) {
                    List<SlotBooking> slotBookings = optionalSlotBookingList.get();
                    if (slotBookings.size() > 0) {
                        throw new Exception("Customer already has a booked slot Exception");
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

                //parkingSlotRepository.save(slotBooking.getParkingSlot());

            } else {
                //Throw ParkingHour slot time Not In Range Exception
                throw new Exception("ParkingHour slot time Not In Range");
            }
        } else {
            //Throw User Not Found Exception
            throw new Exception("User Not Found Exception");
        }
        return bookingResp;
    }

    @Transactional
    public SlotBooking reAllocateParkingSlot(SlotBooking slotBooking) throws Exception{
        SlotBooking bookingResp = new SlotBooking();
        Optional<Customer> customerOptional = customerRepository.findById(slotBooking.getCustomer().getSsNumber());
        if (customerOptional.isPresent()) {
            if (slotBooking.getHourSlot() >= 1 && slotBooking.getHourSlot() <= 4) {
                Optional<ParkingSlot> parkingSlotPresentOpt = parkingSlotRepository.findById(slotBooking.getParkingSlot().getSlotId());
                if (!parkingSlotPresentOpt.isPresent()) {
                    throw new Exception("ParkingSlot Not Found Exception");
                }
                Optional<ParkingSlot> parkingSlotOpt = Optional.ofNullable(parkingSlotRepository.findBySlotIdAndIsBooked(slotBooking.getParkingSlot().getSlotId(), Boolean.TRUE));
                if (parkingSlotOpt.isPresent()) {
                    throw new Exception("ParkingSlot Already Booked Exception");
                }
                Optional<List<SlotBooking>> slotBookingOptional = Optional.ofNullable(slotBookingRepository.findByBookingUniqueId(slotBooking.getBookingUniqueId()));
                if(slotBookingOptional.isPresent()){
                    Optional<List<SlotBooking>> slotBookinUniqueOptional = Optional.ofNullable(slotBookingRepository.findByBookingUniqueIdAndIsBookedNow(slotBooking.getBookingUniqueId(),Boolean.TRUE));
                    if(slotBookinUniqueOptional.isPresent()){
                        List<SlotBooking> slotBookings = slotBookinUniqueOptional.get();
                        if(slotBookings.size()==1){
                            SlotBooking presentSlotBooking = slotBookings.get(0);
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

                            //List<SlotBooking> bookings = Arrays.asList(presentSlotBooking,newSlotBooking);
                            //slotBookingRepository.saveAll(new ArrayList<>(bookings));
                        }else{
                            throw new Exception("Provided bookingUniqueId has issues with allocation");
                        }
                    }else{
                        throw new Exception("Provided bookingUniqueId is not having any valid booking at present or would have elapsed");
                    }
                }else{
                    throw new Exception("Provided bookingUniqueId is not a valid reference,provided for this booking parking slot");
                }


            } else {
                //Throw ParkingHour slot time Not In Range Exception
                throw new Exception("ParkingHour slot time Not In Range");
            }

        } else {
            //Throw User Not Found Exception
            throw new Exception("User Not Found Exception");
        }
        return bookingResp;
    }
}

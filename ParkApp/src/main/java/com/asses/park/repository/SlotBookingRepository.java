package com.asses.park.repository;

import com.asses.park.model.Customer;
import com.asses.park.model.SlotBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SlotBookingRepository extends JpaRepository<SlotBooking,Long> {
    List<SlotBooking> findByCustomerAndIsBookedNow(Customer customer,Boolean isBooked);
    List<SlotBooking> findByBookingUniqueId(UUID bookingUniqueId);
    List<SlotBooking> findByBookingUniqueIdAndIsBookedNow(UUID bookingUniqueId, Boolean isBooked);
}

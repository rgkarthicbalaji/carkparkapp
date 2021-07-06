package com.asses.park.repository;

import com.asses.park.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot,Long> {
    ParkingSlot findBySlotIdAndIsBooked(Long slotId,Boolean isBooked);
}

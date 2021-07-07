package com.asses.park.repository;

import com.asses.park.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot,Long> {
    ParkingSlot findBySlotIdAndIsBooked(Long slotId,Boolean isBooked);
    List<ParkingSlot> findByIsBooked(Boolean isBooked);
}

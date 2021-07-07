package com.asses.park.service;

import com.asses.park.model.Customer;
import com.asses.park.model.ParkingSlot;
import com.asses.park.repository.CustomerRepository;
import com.asses.park.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingSlotService {

    @Autowired
    ParkingSlotRepository parkingSlotRepository;

    public List<ParkingSlot> availableSlots() {
        //TODO: Handle errors
        List<ParkingSlot> parkingSlots = new ArrayList<>();
        Optional<List<ParkingSlot>> optionalParkingSlots = Optional.ofNullable(parkingSlotRepository.findAll());
        if(optionalParkingSlots.isPresent()){
            parkingSlots = optionalParkingSlots.get();
        }

        return parkingSlots;
    }

    public List<ParkingSlot> addSlots(List<ParkingSlot> parkingSlots) throws Exception{
        List<ParkingSlot> parkingSlotsResp = new ArrayList<>();
        parkingSlotsResp = parkingSlotRepository.saveAll(parkingSlots);
        return parkingSlotsResp;
    }
}
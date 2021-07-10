package com.asses.park.service;

import com.asses.park.exception.ParkingSlotNotFoundException;
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

    public List<ParkingSlot> availableSlots() throws Exception{
        List<ParkingSlot> parkingSlots;
        Optional<List<ParkingSlot>> optionalParkingSlots = Optional.ofNullable(parkingSlotRepository.findAll());
        if(optionalParkingSlots.isPresent()){
            parkingSlots = optionalParkingSlots.get();
        }else{
            throw new ParkingSlotNotFoundException();
        }
        return parkingSlots;
    }

    public List<ParkingSlot> addSlots(List<ParkingSlot> parkingSlots){
        List<ParkingSlot> parkingSlotsResp;
        parkingSlotsResp = parkingSlotRepository.saveAll(parkingSlots);
        return parkingSlotsResp;
    }
}
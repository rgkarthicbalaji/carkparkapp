package com.asses.park.util;

import com.asses.park.dto.CustomerInfo;
import com.asses.park.dto.ParkingSlotInfo;
import com.asses.park.dto.SlotBookingInfo;
import com.asses.park.model.Customer;
import com.asses.park.model.CustomerParkingIdentity;
import com.asses.park.model.ParkingSlot;
import com.asses.park.model.SlotBooking;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class Utility {
    public Object convertObject(Object o, TypeReference ref) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.convertValue(o, ref);
    }

    public Function<CustomerInfo,Customer> customerDtoToModel = customerInfo -> {
        Customer customer = new Customer();
        customer.setSsNumber(customerInfo.getSsNumber());
        customer.setEmail(customerInfo.getEmail());
        customer.setFullName(customerInfo.getFullName());
        return customer;
    };

    public Function<Customer,CustomerInfo> customerModelToDto = customer -> {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setSsNumber(customer.getSsNumber());
        customerInfo.setEmail(customer.getEmail());
        customerInfo.setFullName(customer.getFullName());
        return customerInfo;
    };

    public Function<ParkingSlotInfo,ParkingSlot> parkingSlotDtoToModel = parkingSlotInfo -> {
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setSlotId((parkingSlotInfo.getSlotId()));
        parkingSlot.setSlotName(parkingSlotInfo.getSlotName());
        parkingSlot.setIsBooked(parkingSlotInfo.getIsBooked());
        return parkingSlot;
    };

    public Function<List<ParkingSlot>,List<ParkingSlotInfo>> parkingSlotModelListToDto = parkingSlotsInfo -> {
        List<ParkingSlotInfo> parkingSlotInfoList = new ArrayList<>();

        for(ParkingSlot psInfo:parkingSlotsInfo){
            ParkingSlotInfo parkingSlotInfo = new ParkingSlotInfo();
            parkingSlotInfo.setSlotId(psInfo.getSlotId());
            parkingSlotInfo.setSlotName(psInfo.getSlotName());
            parkingSlotInfo.setIsBooked(psInfo.getIsBooked());
            parkingSlotInfoList.add(parkingSlotInfo);
        }


        return parkingSlotInfoList;
    };

    public Function<ParkingSlot,ParkingSlotInfo> parkingSlotModelToDto = parkingSlot -> {
        ParkingSlotInfo parkingSlotInfo = new ParkingSlotInfo();
        parkingSlotInfo.setSlotId((parkingSlot.getSlotId()));
        parkingSlotInfo.setSlotName(parkingSlot.getSlotName());
        parkingSlotInfo.setIsBooked(parkingSlot.getIsBooked());
        return parkingSlotInfo;
    };

    public Function<SlotBookingInfo, SlotBooking> slotBookingDtoToModel
            = slotBookingInfo -> {
                SlotBooking slotBooking = new SlotBooking();
                slotBooking.setBookingUniqueId(slotBookingInfo.getBookingUniqueId());
                slotBooking.setStartTime(slotBookingInfo.getStartTime());
                slotBooking.setEndTime(slotBookingInfo.getEndTime());
                slotBooking.setHourSlot(slotBookingInfo.getHourSlot());

                /*CustomerParkingIdentity parkingIdentity = new CustomerParkingIdentity();
                parkingIdentity.setSlotId(slotBookingInfo.getParkingSlotInfo().getSlotId());
                parkingIdentity.setSsNumber(slotBookingInfo.getCustomerInfo().getSsNumber());
                slotBooking.setIdentity(parkingIdentity);*/

                CustomerInfo customerInfo = new CustomerInfo();
                if(null!=slotBookingInfo.getCustomerInfo()){
                    customerInfo = slotBookingInfo.getCustomerInfo();
                }
                Customer customer = new Customer();
                customer.setSsNumber(customerInfo.getSsNumber());
                customer.setEmail(customerInfo.getEmail());
                customer.setFullName(customerInfo.getFullName());
                slotBooking.setCustomer(customer);

                ParkingSlotInfo parkingSlotInfo = new ParkingSlotInfo();
                if(null!=slotBookingInfo.getParkingSlotInfo()){
                    parkingSlotInfo = slotBookingInfo.getParkingSlotInfo();
                }
                ParkingSlot parkingSlot = new ParkingSlot();
                parkingSlot.setIsBooked(parkingSlotInfo.getIsBooked());
                parkingSlot.setSlotId(parkingSlotInfo.getSlotId());
                parkingSlot.setSlotName(parkingSlotInfo.getSlotName());
                slotBooking.setParkingSlot(parkingSlot);

                return slotBooking;
            };

    public Function<SlotBooking,SlotBookingInfo> slotBookingModelToDto
            = slotBooking -> {
        SlotBookingInfo slotBookingInfo = new SlotBookingInfo();
        slotBookingInfo.setBookingUniqueId(slotBooking.getBookingUniqueId());
        slotBookingInfo.setStartTime(slotBooking.getStartTime());
        slotBookingInfo.setEndTime(slotBooking.getEndTime());
        slotBookingInfo.setHourSlot(slotBooking.getHourSlot());

        /*CustomerParkingIdentity parkingIdentity = new CustomerParkingIdentity();
        parkingIdentity.setSlotId(slotBookingInfo.getParkingSlotInfo().getSlotId());
        parkingIdentity.setSsNumber(slotBookingInfo.getCustomerInfo().getSsNumber());
        slotBooking.setIdentity(parkingIdentity);*/
        Customer customer = new Customer();
        if(null!=slotBooking.getCustomer()){
            customer = slotBooking.getCustomer();
        }
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setSsNumber(customer.getSsNumber());
        customerInfo.setEmail(customer.getEmail());
        customerInfo.setFullName(customer.getFullName());
        slotBookingInfo.setCustomerInfo(customerInfo);

        ParkingSlot parkingSlot = new ParkingSlot();
        if(null!=slotBooking.getParkingSlot()){
            parkingSlot = slotBooking.getParkingSlot();
        }
        ParkingSlotInfo parkingSlotInfo = new ParkingSlotInfo();
        parkingSlotInfo.setIsBooked(parkingSlot.getIsBooked());
        parkingSlotInfo.setSlotId(parkingSlot.getSlotId());
        parkingSlotInfo.setSlotName(parkingSlot.getSlotName());
        slotBookingInfo.setParkingSlotInfo(parkingSlotInfo);

        return slotBookingInfo;
    };
}

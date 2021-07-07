package com.asses.park.controller;

import com.asses.park.dto.CustomResponse;
import com.asses.park.dto.SlotBookingInfo;
import com.asses.park.model.SlotBooking;
import com.asses.park.service.SlotBookingService;
import com.asses.park.util.Utility;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/parking/slot")
@Api(value = "Parking Slot Booking API", description = "Parking Slot Booking API")
@AllArgsConstructor
public class SlotBookingController {
    @Autowired
    private SlotBookingService slotBookingService;

    @Autowired
    private Utility utility;

    @PutMapping(path = "/allocate", produces = {"application/json"}, consumes = {"application/json"})
    @ResponseBody
    //TODO: Check validation on all input parameters
    public ResponseEntity<SlotBookingInfo> allocateParkingSlot(@Valid @RequestBody SlotBookingInfo slotBookingInfoReq) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        slotBookingInfoReq.setStartTime(Timestamp.valueOf(now));
        slotBookingInfoReq.setEndTime(Timestamp.valueOf(now.plusHours(slotBookingInfoReq.getHourSlot())));
        SlotBooking slotBooking = utility.slotBookingDtoToModel.apply(slotBookingInfoReq);
        SlotBooking slotBookingServiceResp = slotBookingService.allocateParkingSlot(slotBooking);
        SlotBookingInfo slotBookingInfoResp = utility.slotBookingModelToDto.apply(slotBookingServiceResp);
        return ResponseEntity.status(HttpStatus.CREATED).body(slotBookingInfoResp);
    }

    @PutMapping(path = "/reallocate", produces = {"application/json"}, consumes = {"application/json"})
    @ResponseBody
    public ResponseEntity<SlotBookingInfo> reAllocateParkingSlot(@Valid @RequestBody SlotBookingInfo slotBookingInfoReq) throws Exception {
        if(null==slotBookingInfoReq.getBookingUniqueId()){
            throw new Exception("Please provide bookingUniqueId and it should be valid one");
        }
        LocalDateTime now = LocalDateTime.now();
        slotBookingInfoReq.setStartTime(Timestamp.valueOf(now));
        slotBookingInfoReq.setEndTime(Timestamp.valueOf(now.plusHours(slotBookingInfoReq.getHourSlot())));
        SlotBooking slotBooking = utility.slotBookingDtoToModel.apply(slotBookingInfoReq);
        SlotBooking slotBookingServiceResp = slotBookingService.reAllocateParkingSlot(slotBooking);
        SlotBookingInfo slotBookingInfoResp = utility.slotBookingModelToDto.apply(slotBookingServiceResp);
        return ResponseEntity.status(HttpStatus.CREATED).body(slotBookingInfoResp);
    }

    @PutMapping(path = "/cancel/", produces = {"application/json"})
    @ResponseBody
    public CustomResponse cancelParkingSlot(@RequestParam UUID bookingUniqueId) throws Exception {
            Long usageTime = slotBookingService.cancelParkingSlot(bookingUniqueId);
            CustomResponse customResponse = new CustomResponse(HttpStatus.OK,"Your Parking Slot Is Cancelled Now, Your Total Parking Usage Time Is "+usageTime+" hrs");
            return customResponse;
    }
}
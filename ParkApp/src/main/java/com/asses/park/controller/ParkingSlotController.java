package com.asses.park.controller;

import com.asses.park.dto.ParkingSlotInfo;
import com.asses.park.model.ParkingSlot;
import com.asses.park.service.ParkingSlotService;
import com.asses.park.util.Utility;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parking")
@Api(value = "Parking Slot API", description = "Parking Slot API")
@AllArgsConstructor
public class ParkingSlotController {
    @Autowired
    private ParkingSlotService parkingSlotService;

    @Autowired
    private Utility utility;

    @GetMapping(path = "/availableSlots", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<ParkingSlotInfo>> availableSlots() throws Exception {
        List<ParkingSlot> parkingSlots = parkingSlotService.availableSlots();
        List<ParkingSlotInfo> parkingSlotInfo = utility.parkingSlotModelListToDto.apply(parkingSlots);
        return ResponseEntity.ok(parkingSlotInfo);
    }

    @PostMapping(path = "/addSlots", produces = {"application/json"}, consumes = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<ParkingSlotInfo>> addSlots(@RequestBody List<ParkingSlotInfo> parkingSlotInfos) throws Exception {
        if(parkingSlotInfos.size()<=0){
            throw new Exception("Need to pass more than one ParkingSlotInfo(s)");
        }
        List<ParkingSlot> parkingSlots = utility.parkingSlotDtoListToModel.apply(parkingSlotInfos);
        List<ParkingSlot> parkingSlotsResp = parkingSlotService.addSlots(parkingSlots);
        List<ParkingSlotInfo> parkingSlotInfosResp = utility.parkingSlotModelListToDto.apply(parkingSlotsResp);
        return ResponseEntity.ok(parkingSlotInfosResp);
    }
}

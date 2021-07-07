package com.asses.park.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SlotBookingInfo implements Serializable {

    private UUID bookingUniqueId;

    private ParkingSlotInfo parkingSlotInfo;

    private CustomerInfo customerInfo;

    @NotNull(message = "hourSlot should not be empty and accepted only valid of its in hourSlot>=1 and hourSlot<=4 range")
    @Min(value = 1,message = "hourSlot is valid only if its always greater than or equal to 1")
    @Max(value = 4,message = "hourSlot is valid only if its always lesser than or equal to 4")
    private Long hourSlot;

    private Timestamp startTime;

    private Timestamp endTime;

}

package com.asses.park.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParkingSlotInfo implements Serializable {
    @Min(value = 1,message = "slotId always starts with positive number and greater than 1")
    @NotNull(message = "slotId should be given as number E.g 1")
    private Long slotId;
    private String slotName;
    private Boolean isBooked;
}
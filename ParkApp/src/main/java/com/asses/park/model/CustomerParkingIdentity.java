package com.asses.park.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CustomerParkingIdentity implements Serializable {
    @Column(name = "SS_NUMBER")
    private Long ssNumber;

    @Column(name = "SLOT_ID")
    private Long slotId;
}

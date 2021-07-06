package com.asses.park.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PARKING_SLOT")
public class ParkingSlot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "SLOT_ID")
    private Long slotId;

    @Column(name = "SLOT_NAME")
    private String slotName;

    @Column(name = "IS_BOOKED")
    private Boolean isBooked;

    @OneToMany(mappedBy = "parkingSlot", cascade = CascadeType.ALL)
    private Set<SlotBooking> slotBookings;
}
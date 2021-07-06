package com.asses.park.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "SLOT_BOOKING")
public class SlotBooking implements Serializable {
    /*@EmbeddedId
    private CustomerParkingIdentity identity;*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "BOOKING_UUID")
    private UUID bookingUniqueId;

    @Column(name = "START_TIME")
    private Timestamp startTime;

    @Column(name = "END_TIME")
    private Timestamp endTime;

    @Column(name = "HOUR_SLOT")
    private Long hourSlot;

    @Column(name = "IS_BOOKED_NOW")
    private Boolean isBookedNow;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_ssNumber")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parkingSlot_slotId")
    private ParkingSlot parkingSlot;

    public SlotBooking(/*CustomerParkingIdentity identity, */UUID bookingUniqueId, Timestamp startTime, Timestamp endTime, Long hourSlot, Boolean isBookedNow, Customer customer, ParkingSlot parkingSlot) {
        this.bookingUniqueId = bookingUniqueId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hourSlot = hourSlot;
        this.isBookedNow = isBookedNow;
        this.customer = customer;
        this.parkingSlot = parkingSlot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotBooking that = (SlotBooking) o;
        return //Objects.equals(identity, that.identity) &&
                Objects.equals(bookingUniqueId, that.bookingUniqueId) &&
                        Objects.equals(startTime, that.startTime) &&
                        Objects.equals(endTime, that.endTime) &&
                        Objects.equals(hourSlot, that.hourSlot) &&
                        Objects.equals(isBookedNow, this.getIsBookedNow()) &&
                        Objects.equals(customer.getSsNumber(), that.customer.getSsNumber()) &&
                        Objects.equals(parkingSlot.getSlotId(), that.parkingSlot.getSlotId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(/*identity,*/ bookingUniqueId, startTime, endTime, hourSlot, isBookedNow, customer.getSsNumber(), parkingSlot.getSlotId());
    }
}

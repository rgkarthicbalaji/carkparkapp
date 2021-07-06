package com.asses.park.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CUSTOMER")
public class Customer implements Serializable {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SS_NUMBER")
    private Long ssNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FULL_NAME")
    private String fullName;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private Set<SlotBooking> slotBookings = new HashSet<>();
}
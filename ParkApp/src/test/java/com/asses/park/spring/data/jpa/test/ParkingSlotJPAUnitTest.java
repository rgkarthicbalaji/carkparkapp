package com.asses.park.spring.data.jpa.test;

import com.asses.park.model.ParkingSlot;
import com.asses.park.repository.ParkingSlotRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ParkingSlotJPAUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Test
    public void should_find_no_parking_slot_if_repository_is_empty() {
        Iterable<ParkingSlot> parkingSlots = parkingSlotRepository.findAll();
        assertThat(parkingSlots).isEmpty();
    }

    @Test
    public void should_store_a_parking_slot() {
        ParkingSlot parkingSlot = parkingSlotRepository.save(new ParkingSlot(Long.valueOf(1), "A", Boolean.FALSE,null));
        assertThat(parkingSlot).hasFieldOrPropertyWithValue("slotName", "A");
        assertThat(parkingSlot).hasFieldOrPropertyWithValue("isBooked", Boolean.FALSE);
    }

    @Test
    public void should_find_parking_booked_slots() {
        ParkingSlot parkingSlot1 = new ParkingSlot(Long.valueOf(11), "A", Boolean.TRUE,null);
        entityManager.merge(parkingSlot1);

        ParkingSlot parkingSlot2 = new ParkingSlot(Long.valueOf(12), "B", Boolean.TRUE,null);
        entityManager.merge(parkingSlot2);

        ParkingSlot parkingSlot3 = new ParkingSlot(Long.valueOf(14), "C", Boolean.TRUE,null);
        entityManager.merge(parkingSlot3);

        Iterable<ParkingSlot> parkingSlots = parkingSlotRepository.findByIsBooked(Boolean.TRUE);

        assertThat(parkingSlots).hasSize(3).asList().size();
    }

    @Test
    public void should_find_parking_booked_slots_not_booked() {
        ParkingSlot parkingSlot1 = new ParkingSlot(Long.valueOf(11), "A", Boolean.FALSE,null);
        entityManager.merge(parkingSlot1);

        ParkingSlot parkingSlot2 = new ParkingSlot(Long.valueOf(12), "B", Boolean.FALSE,null);
        entityManager.merge(parkingSlot2);

        ParkingSlot parkingSlot3 = new ParkingSlot(Long.valueOf(14), "C", Boolean.FALSE,null);
        entityManager.merge(parkingSlot3);

        Iterable<ParkingSlot> parkingSlots = parkingSlotRepository.findByIsBooked(Boolean.FALSE);

        assertThat(parkingSlots).hasSize(3).asList().size();
    }

    @Test(expected = NumberFormatException.class)
    public void should_throw_exception_invalid_parking_slot_id() {
        ParkingSlot parkingSlot = new ParkingSlot(Long.valueOf("a"), "C", Boolean.FALSE,null);
        entityManager.persist(parkingSlot);
    }

    @Test
    public void should_find_by_parking_slot_by_id_and_is_booked() {
        ParkingSlot parkingSlot = new ParkingSlot(Long.valueOf(21), "E", Boolean.TRUE,null);
        entityManager.merge(parkingSlot);

        ParkingSlot slot = parkingSlotRepository.findBySlotIdAndIsBooked(parkingSlot.getSlotId(),Boolean.TRUE);
        assertThat(parkingSlot.getSlotId()).isEqualTo(21L);
        assertThat(parkingSlot.getSlotName()).isEqualTo("E");
        assertThat(parkingSlot.getIsBooked()).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void should_delete_all_tutorials() {
        entityManager.merge(new ParkingSlot(Long.valueOf(1000), "A", Boolean.FALSE,null));
        entityManager.merge(new ParkingSlot(Long.valueOf(1001), "B", Boolean.FALSE,null));

        parkingSlotRepository.deleteAll();

        assertThat(parkingSlotRepository.findAll()).isEmpty();
    }
}

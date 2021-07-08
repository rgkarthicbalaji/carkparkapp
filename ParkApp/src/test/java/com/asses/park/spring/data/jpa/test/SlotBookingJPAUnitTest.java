package com.asses.park.spring.data.jpa.test;

import com.asses.park.model.Customer;
import com.asses.park.model.ParkingSlot;
import com.asses.park.model.SlotBooking;
import com.asses.park.repository.CustomerRepository;
import com.asses.park.repository.ParkingSlotRepository;
import com.asses.park.repository.SlotBookingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SlotBookingJPAUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SlotBookingRepository slotBookingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Test
    public void should_find_no_slot_booking_if_repository_is_empty() {
        List<SlotBooking> slotBookings = slotBookingRepository.findAll();
        assertThat(slotBookings).isEmpty();
    }

    @Test
    public void should_store_a_slot_booking() {
        UUID uuid = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setSsNumber(1L);
        customer.setEmail("karthic@gmail.com");
        customer.setFullName("karthic");
        entityManager.persist(customer);
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setSlotName("A");
        parkingSlot.setIsBooked(Boolean.TRUE);
        entityManager.persist(parkingSlot);
        SlotBooking slotBooking = entityManager.persist(new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),null,Boolean.TRUE,customer,parkingSlot));
        assertThat(slotBooking).hasFieldOrPropertyWithValue("bookingUniqueId", uuid);
        assertThat(slotBooking).hasFieldOrPropertyWithValue("isBookedNow", Boolean.TRUE);
    }

    @Test
    public void should_find_by_customer_and_by_is_booked_now() {
        UUID uuid = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setSsNumber(1L);
        customer.setEmail("karthic@gmail.com");
        customer.setFullName("karthic");
        Customer customerEntity = entityManager.persist(customer);
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setSlotName("A");
        parkingSlot.setIsBooked(Boolean.TRUE);
        entityManager.persist(parkingSlot);
        SlotBooking slotBooking = entityManager.persist(new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),null,Boolean.TRUE,customer,parkingSlot));

        List<SlotBooking> slotBookings = slotBookingRepository.findByCustomerAndIsBookedNow(customerEntity,Boolean.TRUE);
        assertThat(slotBookings).hasSize(1).asList().size();
        SlotBooking booking = slotBookings.get(0);
        assertThat(booking.getBookingUniqueId()).isEqualTo(slotBooking.getBookingUniqueId());
        assertThat(booking.getIsBookedNow()).isEqualTo(slotBooking.getIsBookedNow());
        assertThat(booking.getCustomer().getFullName().equals(slotBooking.getCustomer().getFullName()));
        assertThat(booking.getParkingSlot().getSlotName().equals(slotBooking.getParkingSlot().getSlotName()));
    }

    @Test
    public void should_find_by_booking_unique_id() {
        UUID uuid = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setSsNumber(1L);
        customer.setEmail("karthic@gmail.com");
        customer.setFullName("karthic");
        entityManager.persist(customer);
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setSlotName("A");
        parkingSlot.setIsBooked(Boolean.TRUE);
        entityManager.persist(parkingSlot);
        SlotBooking slotBooking = entityManager.persist(new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),null,Boolean.TRUE,customer,parkingSlot));

        List<SlotBooking> slotBookings = slotBookingRepository.findByBookingUniqueId(uuid);
        assertThat(slotBookings).hasSize(1).asList().size();
        SlotBooking booking = slotBookings.get(0);
        assertThat(booking.getBookingUniqueId()).isEqualTo(slotBooking.getBookingUniqueId());
        assertThat(booking.getIsBookedNow()).isEqualTo(slotBooking.getIsBookedNow());
        assertThat(booking.getCustomer().getFullName().equals(slotBooking.getCustomer().getFullName()));
        assertThat(booking.getParkingSlot().getSlotName().equals(slotBooking.getParkingSlot().getSlotName()));
    }

    @Test
    public void should_find_by_booking_unique_id_and_is_booked_now() {
        UUID uuid = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setSsNumber(1L);
        customer.setEmail("karthic@gmail.com");
        customer.setFullName("karthic");
        entityManager.persist(customer);
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setSlotName("A");
        parkingSlot.setIsBooked(Boolean.TRUE);
        entityManager.persist(parkingSlot);
        SlotBooking slotBooking = entityManager.persist(new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),null,Boolean.TRUE,customer,parkingSlot));

        List<SlotBooking> slotBookings = slotBookingRepository.findByBookingUniqueIdAndIsBookedNow(uuid,Boolean.TRUE);
        assertThat(slotBookings).hasSize(1).asList().size();
        SlotBooking booking = slotBookings.get(0);
        assertThat(booking.getBookingUniqueId()).isEqualTo(slotBooking.getBookingUniqueId());
        assertThat(booking.getIsBookedNow()).isEqualTo(slotBooking.getIsBookedNow());
        assertThat(booking.getCustomer().getFullName().equals(slotBooking.getCustomer().getFullName()));
        assertThat(booking.getParkingSlot().getSlotName().equals(slotBooking.getParkingSlot().getSlotName()));
    }

    @Test
    public void should_find_by_is_booked_now() {
        UUID uuid = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setSsNumber(1L);
        customer.setEmail("karthic@gmail.com");
        customer.setFullName("karthic");
        entityManager.persist(customer);
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setSlotName("A");
        parkingSlot.setIsBooked(Boolean.TRUE);
        entityManager.persist(parkingSlot);
        SlotBooking slotBooking = entityManager.persist(new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),null,Boolean.TRUE,customer,parkingSlot));

        List<SlotBooking> slotBookings = slotBookingRepository.findByIsBookedNow(Boolean.TRUE);
        assertThat(slotBookings).hasSize(1).asList().size();
        SlotBooking booking = slotBookings.get(0);
        assertThat(booking.getBookingUniqueId()).isEqualTo(slotBooking.getBookingUniqueId());
        assertThat(booking.getIsBookedNow()).isEqualTo(slotBooking.getIsBookedNow());
        assertThat(booking.getCustomer().getFullName().equals(slotBooking.getCustomer().getFullName()));
        assertThat(booking.getParkingSlot().getSlotName().equals(slotBooking.getParkingSlot().getSlotName()));
    }

    @Test
    public void should_delete_slot_booking_by_id() {
        UUID uuid1 = UUID.randomUUID();
        Customer customer1 = new Customer();
        customer1.setSsNumber(1L);
        customer1.setEmail("karthic@gmail.com");
        customer1.setFullName("karthic");
        entityManager.persist(customer1);
        ParkingSlot parkingSlot1 = new ParkingSlot();
        parkingSlot1.setSlotName("A");
        parkingSlot1.setIsBooked(Boolean.TRUE);
        entityManager.persist(parkingSlot1);
        SlotBooking slotBooking1 = entityManager.persist(new SlotBooking(uuid1, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),null,Boolean.TRUE,customer1,parkingSlot1));

        UUID uuid2 = UUID.randomUUID();
        Customer customer2 = new Customer();
        customer2.setSsNumber(2L);
        customer2.setEmail("balaji@gmail.com");
        customer2.setFullName("balaji");
        entityManager.persist(customer2);
        ParkingSlot parkingSlot2 = new ParkingSlot();
        parkingSlot2.setSlotName("B");
        parkingSlot2.setIsBooked(Boolean.TRUE);
        entityManager.persist(parkingSlot2);
        SlotBooking slotBooking2 = entityManager.persist(new SlotBooking(uuid2, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),null,Boolean.TRUE,customer2,parkingSlot2));

        slotBookingRepository.deleteById(slotBooking1.getId());

        List<SlotBooking> slotBookings = slotBookingRepository.findAll();

        assertThat(slotBookings).hasSize(1).contains(slotBooking2);
    }

    @Test
    public void should_delete_all_slot_bookings() {
        UUID uuid1 = UUID.randomUUID();
        Customer customer1 = new Customer();
        customer1.setSsNumber(1L);
        customer1.setEmail("karthic@gmail.com");
        customer1.setFullName("karthic");
        entityManager.persist(customer1);
        ParkingSlot parkingSlot1 = new ParkingSlot();
        parkingSlot1.setSlotName("A");
        parkingSlot1.setIsBooked(Boolean.TRUE);
        entityManager.persist(parkingSlot1);
        SlotBooking slotBooking1 = entityManager.persist(new SlotBooking(uuid1, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),null,Boolean.TRUE,customer1,parkingSlot1));

        UUID uuid2 = UUID.randomUUID();
        Customer customer2 = new Customer();
        customer2.setSsNumber(2L);
        customer2.setEmail("balaji@gmail.com");
        customer2.setFullName("balaji");
        entityManager.persist(customer2);
        ParkingSlot parkingSlot2 = new ParkingSlot();
        parkingSlot2.setSlotName("B");
        parkingSlot2.setIsBooked(Boolean.TRUE);
        entityManager.persist(parkingSlot2);
        SlotBooking slotBooking2 = entityManager.persist(new SlotBooking(uuid2, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),null,Boolean.TRUE,customer2,parkingSlot2));

        slotBookingRepository.deleteAll();

        assertThat(slotBookingRepository.findAll()).isEmpty();
    }

}

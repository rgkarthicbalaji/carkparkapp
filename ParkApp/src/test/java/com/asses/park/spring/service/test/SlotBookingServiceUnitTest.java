package com.asses.park.spring.service.test;

import com.asses.park.exception.*;
import com.asses.park.model.Customer;
import com.asses.park.model.ParkingSlot;
import com.asses.park.model.SlotBooking;
import com.asses.park.repository.CustomerRepository;
import com.asses.park.repository.ParkingSlotRepository;
import com.asses.park.repository.SlotBookingRepository;
import com.asses.park.service.CustomerService;
import com.asses.park.service.ParkingSlotService;
import com.asses.park.service.SlotBookingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class SlotBookingServiceUnitTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ParkingSlotService parkingSlotService;

    @Autowired
    private SlotBookingService slotBookingService;

    @TestConfiguration
    static class SlotBookingTestConfiguration {
        @Bean
        public CustomerService customerService() {
            return new CustomerService();
        }

        @Bean
        public ParkingSlotService parkingSlotService() {
            return new ParkingSlotService();
        }

        @Bean
        public SlotBookingService slotBookingService() {
            return new SlotBookingService();
        }
    }

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private ParkingSlotRepository parkingSlotRepository;

    @MockBean
    private SlotBookingRepository slotBookingRepository;

    private UUID uuid = UUID.randomUUID();

    @Test
    public void when_slotbooking_is_valid_perform_allocate() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.FALSE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(slotBookingRepository.findById(1L)).thenReturn(java.util.Optional.of(slotBooking));
        Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        Mockito.when(parkingSlotRepository.findById(slotBooking.getParkingSlot().getSlotId())).thenReturn(java.util.Optional.of(parkingSlotA));
        Mockito.when(slotBookingRepository.findByCustomerAndIsBookedNow(slotBooking.getCustomer(),Boolean.FALSE)).thenReturn(Arrays.asList());
        Mockito.when(slotBookingRepository.save(slotBooking)).thenReturn(slotBooking);
        SlotBooking booking = slotBookingService.allocateParkingSlot(slotBooking);
        assertThat(booking).extracting(SlotBooking::getCustomer).isEqualTo(customer);
        assertThat(booking).extracting(SlotBooking::getParkingSlot).isEqualTo(parkingSlotA);
        assertThat(booking).extracting(SlotBooking::getIsBookedNow).isEqualTo(Boolean.TRUE);
    }

    //Invalid Customer for allocate
    @Test(expected = CustomerNotFoundException.class)
    public void when_allocate_customer_not_registered_then_throw_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.FALSE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        slotBookingService.allocateParkingSlot(slotBooking);
    }

    //Hourslot is lesser than 1 hour
    @Test(expected = ParkingSlotTimeNotInRageException.class)
    public void when_allocate_slotbooking_hourslot_is_lesser_then_min_throw_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.FALSE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),0L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        slotBookingService.allocateParkingSlot(slotBooking);
    }

    //Hourslot is greater than 4 hours
    @Test(expected = ParkingSlotTimeNotInRageException.class)
    public void when_allocate_slotbooking_hourslot_is_lesser_then_max_throw_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.FALSE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),5L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        slotBookingService.allocateParkingSlot(slotBooking);
    }

    //Pass invalid non-existing parking slot for booking
    @Test(expected = ParkingSlotNotFoundException.class)
    public void when_allocate_slotbooking_invalid_parkingslot_given_then_throw_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(10000L, "A", Boolean.FALSE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        slotBookingService.allocateParkingSlot(slotBooking);
    }

    //Provide parking slot which is already occupied for booking
    @Test(expected = ParkingSlotAlreadyBookedException.class)
    public void when_allocate_slotbooking_provide_preoccupied_parkingslot_then_throw_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.TRUE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        Mockito.when(parkingSlotRepository.findById(slotBooking.getParkingSlot().getSlotId())).thenReturn(java.util.Optional.of(parkingSlotA));
        Mockito.when(parkingSlotRepository.findBySlotIdAndIsBooked(slotBooking.getParkingSlot().getSlotId(), Boolean.TRUE)).thenReturn(parkingSlotA);
        slotBookingService.allocateParkingSlot(slotBooking);
    }

    //Provide customer who already occupied a parking slot for booking
    @Test(expected = CustomerAlreadyOwnsSlotException.class)
    public void when_allocate_slotbooking_provide_slot_preoccupied_customer_then_throw_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.FALSE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        Mockito.when(parkingSlotRepository.findById(slotBooking.getParkingSlot().getSlotId())).thenReturn(java.util.Optional.of(parkingSlotA));
        Mockito.when(parkingSlotRepository.findBySlotIdAndIsBooked(slotBooking.getParkingSlot().getSlotId(), Boolean.FALSE)).thenReturn(parkingSlotA);
        Mockito.when(slotBookingRepository.findByCustomerAndIsBookedNow(slotBooking.getCustomer(),Boolean.TRUE)).thenReturn(Arrays.asList(slotBooking));
        slotBookingService.allocateParkingSlot(slotBooking);
    }

    @Test
    public void when_slotbooking_is_valid_perform_reallocate() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.TRUE,null);
        ParkingSlot parkingSlotB = new ParkingSlot(1L, "B", Boolean.TRUE,null);

        SlotBooking presentSlotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().minusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        SlotBooking newSlotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotB);
        Mockito.when(slotBookingRepository.findById(1L)).thenReturn(java.util.Optional.of(presentSlotBooking));
        Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        Mockito.when(parkingSlotRepository.findById(presentSlotBooking.getParkingSlot().getSlotId())).thenReturn(java.util.Optional.of(parkingSlotA));
        Mockito.when(slotBookingRepository.findByBookingUniqueIdAndIsBookedNow(presentSlotBooking.getBookingUniqueId(), Boolean.TRUE)).thenReturn(Arrays.asList(presentSlotBooking));
        Mockito.when(parkingSlotRepository.findById(presentSlotBooking.getParkingSlot().getSlotId())).thenReturn(java.util.Optional.of(parkingSlotB));
        parkingSlotA.setIsBooked(Boolean.FALSE);
        presentSlotBooking.setParkingSlot(parkingSlotA);
        Mockito.when(slotBookingRepository.saveAndFlush(presentSlotBooking)).thenReturn(presentSlotBooking).thenReturn(newSlotBooking);
        slotBookingRepository.saveAndFlush(presentSlotBooking);
        slotBookingRepository.saveAndFlush(newSlotBooking);
        SlotBooking resultBooking = slotBookingService.reAllocateParkingSlot(presentSlotBooking);

        assertThat(resultBooking).extracting(SlotBooking::getCustomer).isEqualTo(customer);
        assertThat(resultBooking).extracting(SlotBooking::getParkingSlot).isEqualTo(parkingSlotB);
        assertThat(resultBooking).extracting(SlotBooking::getIsBookedNow).isEqualTo(Boolean.TRUE);
    }

    //Invalid Customer for reallocate
    @Test(expected = CustomerNotFoundException.class)
    public void when_reallocate_customer_non_registered_provided_throw_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.FALSE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        slotBookingService.reAllocateParkingSlot(slotBooking);
    }

    //Hourslot is lesser than 1 hour for reallocate
    @Test(expected = ParkingSlotTimeNotInRageException.class)
    public void when_reallocate_slotbooking_hourslot_is_lesser_then_min_throw_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.FALSE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),0L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        slotBookingService.reAllocateParkingSlot(slotBooking);
    }

    //Hourslot is greater than 4 hours for reallocate
    @Test(expected = ParkingSlotTimeNotInRageException.class)
    public void when_reallocate_slotbooking_hourslot_is_lesser_then_max_throw_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.FALSE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),5L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        slotBookingService.reAllocateParkingSlot(slotBooking);
    }

    //Pass invalid non-existing parking slot for reallocate
    @Test(expected = ParkingSlotNotFoundException.class)
    public void when_reallocate_slotbooking_invalid_parkingslot_given_then_throw_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(10000L, "A", Boolean.FALSE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        slotBookingService.reAllocateParkingSlot(slotBooking);
    }

    //Provide parking slot which is already occupied for reallocate
    @Test(expected = ParkingSlotAlreadyBookedException.class)
    public void when_reallocate_slotbooking_provide_preoccupied_parkingslot_then_throw_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.TRUE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        Mockito.when(parkingSlotRepository.findById(slotBooking.getParkingSlot().getSlotId())).thenReturn(java.util.Optional.of(parkingSlotA));
        Mockito.when(parkingSlotRepository.findBySlotIdAndIsBooked(slotBooking.getParkingSlot().getSlotId(), Boolean.TRUE)).thenReturn(parkingSlotA);
        slotBookingService.reAllocateParkingSlot(slotBooking);
    }

    @Test(expected = BookingParamsNotValidException.class)
    public void when_reallocate_slotbooking_provide_invalid_bookingUniqueId_thow_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.TRUE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().minusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        SlotBooking slotBookingInvalid = new SlotBooking(UUID.randomUUID(), Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().minusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(slotBookingRepository.findById(1L)).thenReturn(java.util.Optional.of(slotBooking));
        Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        Mockito.when(parkingSlotRepository.findById(slotBooking.getParkingSlot().getSlotId())).thenReturn(java.util.Optional.of(parkingSlotA));
        //Mockito.when(slotBookingRepository.findByBookingUniqueId(slotBooking.getBookingUniqueId())).thenReturn(Arrays.asList(slotBookingInvalid));
        slotBookingService.reAllocateParkingSlot(slotBooking);
    }

    @Test(expected = BookingParamsNotValidException.class)
    public void when_reallocate_slotbooking_valid_elapsed_bookingUniqueId_thow_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.TRUE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().minusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        SlotBooking slotBookingInvalid = new SlotBooking(UUID.randomUUID(), Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().minusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(slotBookingRepository.findById(1L)).thenReturn(java.util.Optional.of(slotBooking));
        Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        Mockito.when(parkingSlotRepository.findById(slotBooking.getParkingSlot().getSlotId())).thenReturn(java.util.Optional.of(parkingSlotA));
        Mockito.when(slotBookingRepository.findByBookingUniqueId(uuid)).thenReturn(Arrays.asList(slotBookingInvalid));
        slotBookingService.reAllocateParkingSlot(slotBooking);
    }

    @Test(expected = BookingParamsNotValidException.class)
    public void when_reallocate_slotbooking_endTime_yet_to_elapse_thow_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.TRUE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(slotBookingRepository.findById(1L)).thenReturn(java.util.Optional.of(slotBooking));
        Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
        Mockito.when(parkingSlotRepository.findById(slotBooking.getParkingSlot().getSlotId())).thenReturn(java.util.Optional.of(parkingSlotA));
        Mockito.when(slotBookingRepository.findByBookingUniqueIdAndIsBookedNow(slotBooking.getBookingUniqueId(), Boolean.TRUE)).thenReturn(Arrays.asList(slotBooking));
        slotBookingService.reAllocateParkingSlot(slotBooking);
    }

    @Test
    public void when_slotbooking_is_valid_perform_cancel() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.TRUE,null);
        UUID uniqueBookingId = UUID.randomUUID();
        SlotBooking slotBooking = new SlotBooking(uniqueBookingId, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(slotBookingRepository.findByBookingUniqueId(uniqueBookingId)).thenReturn(Arrays.asList(slotBooking));
        Mockito.when(slotBookingRepository.findByBookingUniqueIdAndIsBookedNow(slotBooking.getBookingUniqueId(), Boolean.TRUE)).thenReturn(Arrays.asList(slotBooking));
        parkingSlotA.setIsBooked(Boolean.FALSE);
        slotBooking.setParkingSlot(parkingSlotA);
        Mockito.when(slotBookingRepository.saveAndFlush(slotBooking)).thenReturn(slotBooking);
        Long result = slotBookingService.cancelParkingSlot(uniqueBookingId);

        assertThat(result==1L);
    }

    @Test(expected = BookingParamsNotValidException.class)
    public void when_cancel_slotbooking_input_invalid_uniqueBookingId_thow_exception() throws Exception{
        slotBookingService.cancelParkingSlot(UUID.randomUUID());
    }

    @Test(expected = BookingParamsNotValidException.class)
    public void when_cancel_slotbooking_input_elapsed_uniqueBookingId_thow_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.TRUE,null);
        UUID uniqueBookingId = UUID.randomUUID();
        SlotBooking slotBooking = new SlotBooking(uniqueBookingId, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(slotBookingRepository.findByBookingUniqueId(uniqueBookingId)).thenReturn(Arrays.asList(slotBooking));
        slotBookingService.cancelParkingSlot(uniqueBookingId);
    }

    @Test(expected = BookingParamsNotValidException.class)
    public void when_cancel_slotbooking_with_valid_uniqueBookingId_and_cancelled_slot_thow_exception() throws Exception{
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(1L, "A", Boolean.TRUE,null);
        UUID uniqueBookingId = UUID.randomUUID();
        SlotBooking slotBooking = new SlotBooking(uniqueBookingId, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(1L)),1L,Boolean.TRUE,customer,parkingSlotA);
        Mockito.when(slotBookingRepository.findByBookingUniqueId(uniqueBookingId)).thenReturn(Arrays.asList(slotBooking));
        Mockito.when(slotBookingRepository.findByBookingUniqueIdAndIsBookedNow(slotBooking.getBookingUniqueId(), Boolean.TRUE)).thenReturn(Arrays.asList(slotBooking,slotBooking));
        slotBookingService.cancelParkingSlot(uniqueBookingId);
    }
}
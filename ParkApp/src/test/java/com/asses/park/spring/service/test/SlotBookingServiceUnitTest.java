package com.asses.park.spring.service.test;

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
    public void when_slotbooking_is_valid_perform_booking() throws Exception{
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

}

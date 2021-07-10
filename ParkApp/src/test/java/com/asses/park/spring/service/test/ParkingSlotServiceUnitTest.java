package com.asses.park.spring.service.test;

import com.asses.park.model.ParkingSlot;
import com.asses.park.repository.ParkingSlotRepository;
import com.asses.park.service.ParkingSlotService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ParkingSlotServiceUnitTest {

    @Autowired
    private ParkingSlotService parkingSlotService;

    @TestConfiguration
    static class ParkingSlotTestConfiguration {
        @Bean
        public ParkingSlotService parkingSlotService() {
            return new ParkingSlotService();
        }
    }

    @MockBean
    private ParkingSlotRepository parkingSlotRepository;

    @Before
    public void setUp(){
        ParkingSlot parkingSlotA = new ParkingSlot(Long.valueOf(1), "A", Boolean.TRUE,null);
        List<ParkingSlot> availableSlots = Arrays.asList(parkingSlotA);
        Mockito.when(parkingSlotRepository.findAll()).thenReturn(availableSlots);
        Mockito.when(parkingSlotRepository.saveAll(availableSlots)).thenReturn(availableSlots);
    }

    @Test
    public void when_parking_slot_not_present_throw_exception() throws Exception{
        ParkingSlot parkingSlot = new ParkingSlot(1L, "A", Boolean.TRUE,null);
        assertThat(parkingSlotService.availableSlots()).hasSize(1);
    }

    @Test
    public void when_valid_parking_slot_then_check() throws Exception{
        ParkingSlot parkingSlot = new ParkingSlot(1L, "A", Boolean.TRUE,null);
        assertThat(parkingSlotService.addSlots(Arrays.asList(parkingSlot))).extracting(ParkingSlot::getSlotName).contains(parkingSlot.getSlotName());
    }
}

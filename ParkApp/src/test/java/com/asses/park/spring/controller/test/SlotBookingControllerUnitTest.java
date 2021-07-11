package com.asses.park.spring.controller.test;

import com.asses.park.controller.SlotBookingController;
import com.asses.park.dto.CustomerInfo;
import com.asses.park.dto.ParkingSlotInfo;
import com.asses.park.dto.SlotBookingInfo;
import com.asses.park.model.Customer;
import com.asses.park.model.ParkingSlot;
import com.asses.park.model.SlotBooking;
import com.asses.park.service.SlotBookingService;
import com.asses.park.spring.controller.test.util.JsonUtil;
import com.asses.park.util.Utility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = SlotBookingController.class)
public class SlotBookingControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SlotBookingService slotBookingService;

    @MockBean
    private Utility utility;

    @Before
    public void setUp() throws Exception {
        utility.customerDtoToModel = mock(Function.class);
        utility.customerModelToDto = mock(Function.class);
        utility.parkingSlotDtoToModel = mock(Function.class);
        utility.parkingSlotModelToDto = mock(Function.class);
        utility.parkingSlotDtoListToModel = mock(Function.class);
        utility.parkingSlotModelListToDto = mock(Function.class);
        utility.slotBookingDtoToModel = mock(Function.class);
        utility.slotBookingModelToDto = mock(Function.class);
    }

    @Test
    public void when_valid_slotbooking_then_book_slot() throws Exception {

        UUID uuid = UUID.randomUUID();

        CustomerInfo customerInfo = new CustomerInfo(Long.valueOf(1), "karthic@gmail.com", "karthic");
        ParkingSlotInfo parkingSlotInfo = new ParkingSlotInfo(Long.valueOf(1), "A", Boolean.TRUE);

        SlotBookingInfo slotBookingInfo = new SlotBookingInfo(uuid, parkingSlotInfo,customerInfo,Long.valueOf(1),Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(Long.valueOf(1))));

        Customer customer = new Customer(Long.valueOf(1), "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(Long.valueOf(1), "A", Boolean.TRUE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(Long.valueOf(1))),Long.valueOf(1),Boolean.TRUE,customer,parkingSlotA);

        given(slotBookingService.allocateParkingSlot(Mockito.any())).willReturn(slotBooking);
        given(utility.slotBookingDtoToModel.apply(slotBookingInfo)).willReturn(slotBooking);
        given(utility.slotBookingModelToDto.apply(slotBooking)).willReturn(slotBookingInfo);

        mockMvc.perform(put("/api/v1/parking/slot/allocate").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(slotBookingInfo))).andExpect(status().isCreated()).andExpect(jsonPath("$.bookingUniqueId", is(uuid.toString())));
        verify(slotBookingService, VerificationModeFactory.times(1)).allocateParkingSlot(Mockito.any());
        reset(slotBookingService);
    }

    @Test
    public void when_allocate_slot_booking_hourslot_is_null_then_throw_validation_exception() throws Exception{
        UUID uuid = UUID.randomUUID();
        CustomerInfo customerInfo = new CustomerInfo(Long.valueOf(1), "karthic@gmail.com", "karthic");
        ParkingSlotInfo parkingSlotInfo = new ParkingSlotInfo(Long.valueOf(1), "A", Boolean.TRUE);

        SlotBookingInfo slotBookingInfo = new SlotBookingInfo(uuid, parkingSlotInfo,customerInfo,null,Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(Long.valueOf(1))));

        mockMvc.perform(put("/api/v1/parking/slot/allocate").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(slotBookingInfo))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is("hourSlot should not be empty and accepted only valid of its in hourSlot>=1 and hourSlot<=4 range")));
        reset(slotBookingService);
    }

    @Test
    public void when_allocate_slot_booking_hourslot_is_less_than_min_throw_validation_exception() throws Exception{
        UUID uuid = UUID.randomUUID();
        CustomerInfo customerInfo = new CustomerInfo(Long.valueOf(1), "karthic@gmail.com", "karthic");
        ParkingSlotInfo parkingSlotInfo = new ParkingSlotInfo(Long.valueOf(1), "A", Boolean.TRUE);

        SlotBookingInfo slotBookingInfo = new SlotBookingInfo(uuid, parkingSlotInfo,customerInfo,Long.valueOf(0L),Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(Long.valueOf(1))));

        mockMvc.perform(put("/api/v1/parking/slot/allocate").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(slotBookingInfo))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is("hourSlot is valid only if its always greater than or equal to 1")));
        reset(slotBookingService);
    }

    @Test
    public void when_allocate_slot_booking_hourslot_is_greater_than_max_throw_validation_exception() throws Exception{
        UUID uuid = UUID.randomUUID();
        CustomerInfo customerInfo = new CustomerInfo(Long.valueOf(1), "karthic@gmail.com", "karthic");
        ParkingSlotInfo parkingSlotInfo = new ParkingSlotInfo(Long.valueOf(1), "A", Boolean.TRUE);

        SlotBookingInfo slotBookingInfo = new SlotBookingInfo(uuid, parkingSlotInfo,customerInfo,Long.valueOf(5L),Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(Long.valueOf(1))));

        mockMvc.perform(put("/api/v1/parking/slot/allocate").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(slotBookingInfo))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is("hourSlot is valid only if its always lesser than or equal to 4")));
        reset(slotBookingService);
    }

    @Test
    public void when_valid_slot_booking_then_reallocate() throws Exception {

        UUID uuid = UUID.randomUUID();

        CustomerInfo customerInfo = new CustomerInfo(Long.valueOf(1), "karthic@gmail.com", "karthic");
        ParkingSlotInfo parkingSlotInfo = new ParkingSlotInfo(Long.valueOf(1), "A", Boolean.TRUE);

        SlotBookingInfo slotBookingInfo = new SlotBookingInfo(uuid, parkingSlotInfo,customerInfo,Long.valueOf(1),Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(Long.valueOf(1))));

        Customer customer = new Customer(Long.valueOf(1), "karthic@gmail.com", "karthic",null);
        ParkingSlot parkingSlotA = new ParkingSlot(Long.valueOf(1), "B", Boolean.TRUE,null);

        SlotBooking slotBooking = new SlotBooking(uuid, Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(Long.valueOf(1))),Long.valueOf(1),Boolean.TRUE,customer,parkingSlotA);

        given(slotBookingService.reAllocateParkingSlot(Mockito.any())).willReturn(slotBooking);
        given(utility.slotBookingDtoToModel.apply(slotBookingInfo)).willReturn(slotBooking);

        ParkingSlotInfo newParkingSlotInfo = new ParkingSlotInfo(Long.valueOf(2), "B", Boolean.TRUE);
        SlotBookingInfo newSlotBookingInfo = new SlotBookingInfo(uuid, newParkingSlotInfo,customerInfo,Long.valueOf(1),Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(Long.valueOf(1))));
        given(utility.slotBookingModelToDto.apply(slotBooking)).willReturn(newSlotBookingInfo);

        mockMvc.perform(put("/api/v1/parking/slot/reallocate").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(slotBookingInfo))).andExpect(status().isCreated()).andExpect(jsonPath("$.bookingUniqueId", is(uuid.toString()))).andExpect(jsonPath("$.parkingSlotInfo.slotName", is("B"))).andExpect(jsonPath("$.parkingSlotInfo.isBooked", is(true)));
        verify(slotBookingService, VerificationModeFactory.times(1)).reAllocateParkingSlot(Mockito.any());
        reset(slotBookingService);
    }

    @Test
    public void when_reallocate_slot_booking_bookingUniqueId_is_null_then_throw_validation_exception() throws Exception{
        UUID uuid = UUID.randomUUID();
        CustomerInfo customerInfo = new CustomerInfo(Long.valueOf(1), "karthic@gmail.com", "karthic");
        ParkingSlotInfo parkingSlotInfo = new ParkingSlotInfo(Long.valueOf(1), "A", Boolean.TRUE);

        SlotBookingInfo slotBookingInfo = new SlotBookingInfo(null, parkingSlotInfo,customerInfo,Long.valueOf(1),Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now().plusHours(Long.valueOf(1))));

        mockMvc.perform(put("/api/v1/parking/slot/reallocate").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(slotBookingInfo))).andExpect(status().isForbidden()).andExpect(jsonPath("$.message", is("Please provide bookingUniqueId and it should be valid one")));
        reset(slotBookingService);
    }

    @Test
    public void when_cancel_slot_booking_valid_bookingUniqueId_then_do_cancel() throws Exception{
        UUID uuid = UUID.randomUUID();
        given(slotBookingService.cancelParkingSlot(Mockito.any())).willReturn(Long.valueOf(1));
        mockMvc.perform(put("/api/v1/parking/slot/cancel/").param("bookingUniqueId",uuid.toString())).andExpect(status().isOk()).andExpect(jsonPath("$.message", is("Your Parking Slot Is Cancelled Now, Your Total Parking Usage Time Is 1 hrs")));
        reset(slotBookingService);
    }

    @Test
    public void when_cancel_slot_booking_bookingUniqueId_is_null_then_throw_validation_exception() throws Exception{
        UUID uuid = UUID.randomUUID();
        mockMvc.perform(put("/api/v1/parking/slot/cancel/").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(uuid))).andExpect(status().isBadRequest());
        reset(slotBookingService);
    }
}

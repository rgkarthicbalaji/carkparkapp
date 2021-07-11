package com.asses.park.spring.controller.test;

import com.asses.park.controller.ParkingSlotController;
import com.asses.park.dto.CustomerInfo;
import com.asses.park.dto.ParkingSlotInfo;
import com.asses.park.model.Customer;
import com.asses.park.model.ParkingSlot;
import com.asses.park.service.CustomerService;
import com.asses.park.service.ParkingSlotService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ParkingSlotController.class)
public class ParkingSlotControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingSlotService parkingSlotService;

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
    public void when_valid_customer_Post_then_addSlots() throws Exception {
        ParkingSlot parkingSlotA = new ParkingSlot(Long.valueOf(1), "A", Boolean.TRUE,null);
        List<ParkingSlot> parkingSlots = Arrays.asList(parkingSlotA);
        ParkingSlotInfo parkingSlotInfo = new ParkingSlotInfo(Long.valueOf(1), "A", Boolean.TRUE);
        List<ParkingSlotInfo> parkingSlotInfos = new ArrayList<>(Arrays.asList(parkingSlotInfo));
        given(utility.parkingSlotDtoListToModel.apply(parkingSlotInfos)).willReturn(parkingSlots);
        given(parkingSlotService.addSlots(Mockito.any())).willReturn(parkingSlots);
        given(utility.parkingSlotModelListToDto.apply(parkingSlots)).willReturn(parkingSlotInfos);

        mockMvc.perform(post("/api/v1/parking/addSlots").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(parkingSlotInfos))).andExpect(status().isOk()).andExpect(jsonPath("[0].slotName", is("A")));
        verify(parkingSlotService, VerificationModeFactory.times(1)).addSlots(Mockito.any());
        reset(parkingSlotService);
    }

    @Test
    public void when_addSlots_slotId_is_null_then_throw_exception() throws Exception {
        mockMvc.perform(post("/api/v1/parking/addSlots").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(Arrays.asList()))).andExpect(status().isInternalServerError()).andExpect(jsonPath("$.message", is("Need to pass more than one ParkingSlotInfo(s)")));
        reset(parkingSlotService);
    }

}

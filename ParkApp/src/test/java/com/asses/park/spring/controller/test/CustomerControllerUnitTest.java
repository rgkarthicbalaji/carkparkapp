package com.asses.park.spring.controller.test;

import com.asses.park.controller.CustomerController;
import com.asses.park.dto.CustomerInfo;
import com.asses.park.model.Customer;
import com.asses.park.service.CustomerService;
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

import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CustomerController.class)
public class CustomerControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

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
    public void when_valid_customer_Post_then_create_customer() throws Exception {
        CustomerInfo customerInfo = new CustomerInfo(1L, "karthic@gmail.com", "karthic");
        Customer customer = new Customer(1L, "karthic@gmail.com", "karthic",null);
        given(customerService.createParkingCustomer(Mockito.any())).willReturn(customer);
        given(utility.customerDtoToModel.apply(customerInfo)).willReturn(customer);
        given(utility.customerModelToDto.apply(customer)).willReturn(customerInfo);

        mockMvc.perform(post("/api/v1/parking/customer/create").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(customer))).andExpect(status().isCreated()).andExpect(jsonPath("$.fullName", is("karthic")));
        verify(customerService, VerificationModeFactory.times(1)).createParkingCustomer(Mockito.any());
        reset(customerService);
    }

    @Test
    public void when_createCustomer_ssNumber_is_lesser_than_one_then_throw_validation_exception() throws Exception{
        Customer customer = new Customer(0L, "karthic@gmail.com", "karthic",null);

        mockMvc.perform(post("/api/v1/parking/customer/create").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(customer))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is("ssNumber is always starts with positive number and greater than 1")));
        reset(customerService);
    }

    @Test
    public void when_createCustomer_ssNumber_is_null_then_throw_validation_exception() throws Exception{
        Customer customer = new Customer(null, "karthic@gmail.com", "karthic",null);

        mockMvc.perform(post("/api/v1/parking/customer/create").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(customer))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is("ssNumber should be given as number E.g 1234")));
        reset(customerService);
    }

    @Test
    public void when_createCustomer_email_is_null_then_throw_validation_exception() throws Exception {
        Customer customer = new Customer(1L, null, "karthic",null);

        mockMvc.perform(post("/api/v1/parking/customer/create").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(customer))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is("email should not be blank")));
        reset(customerService);
    }

    @Test
    public void when_createCustomer_email_is_empty_string_then_throw_validation_exception() throws Exception {
        Customer customer = new Customer(1L, "", "karthic",null);

        mockMvc.perform(post("/api/v1/parking/customer/create").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(customer))).andExpect(status().isBadRequest());
        reset(customerService);
    }
}

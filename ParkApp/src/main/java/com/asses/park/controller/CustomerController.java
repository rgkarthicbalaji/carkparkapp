package com.asses.park.controller;

import com.asses.park.dto.CustomerInfo;
import com.asses.park.model.Customer;
import com.asses.park.service.CustomerService;
import com.asses.park.util.Utility;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/parking/customer")
@Api(value = "Customer Account API", description = "Customer Account API")
@AllArgsConstructor
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private Utility utility;

    @PostMapping(path = "/create", produces = {"application/json"}, consumes = {"application/json"})
    @ResponseBody
    //TODO: Check validation on all input parameters
    public ResponseEntity<CustomerInfo> createParkingUser(@Valid @RequestBody CustomerInfo customerInfo) throws Exception {
        //Customer customer = (Customer) utlity.convertObject(customerInfo, new TypeReference<Customer>() {});
        Customer customer = utility.customerDtoToModel.apply(customerInfo);
        Customer customerServiceResp = customerService.createParkingUser(customer);
        CustomerInfo parkingUserContrllerResp = utility.customerModelToDto.apply(customerServiceResp);
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingUserContrllerResp);
    }
}

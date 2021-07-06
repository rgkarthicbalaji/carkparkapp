package com.asses.park.service;

import com.asses.park.model.Customer;
import com.asses.park.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    //TODO: Handle error if user already exist
    public Customer createParkingUser(Customer customer) throws Exception{
        Customer customerResponse = new Customer();
        if(!customerRepository.findById(customer.getSsNumber()).isPresent()){
            customerResponse = customerRepository.save(customer);
        }else{
            throw new Exception("User Already Registered");
        }
        return customerResponse;
    }
}
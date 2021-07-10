package com.asses.park.spring.service.test;

import com.asses.park.exception.CustomerAlreadyRegisteredException;
import com.asses.park.model.Customer;
import com.asses.park.repository.CustomerRepository;
import com.asses.park.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class CustomerServiceUnitTest {

    @TestConfiguration
    static class CustomerServiceTestConfiguration {
        @Bean
        public CustomerService customerService() {
            return new CustomerService();
        }
    }

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @Before
    public void setUp(){
        Customer karthic = new Customer(1L, "karthic@gmail.com", "karthic",null);
        Customer createParkingUser = new Customer(2L, "balaji@gmail.com", "balaji",null);
        Mockito.when(customerRepository.findById(Long.valueOf(1))).thenReturn(java.util.Optional.of(karthic));
        Mockito.when(customerRepository.save(createParkingUser)).thenReturn(createParkingUser);
    }

    @Test(expected = CustomerAlreadyRegisteredException.class)
    public void when_existing_customer_then_throw_exception() throws Exception{
        Customer karthic = new Customer(1L, "karthic@gmail.com", "karthic",null);
        customerService.createParkingUser(karthic);
    }

    @Test
    public void when_valid_customer_then_check() throws Exception{
        Customer balaji = new Customer(2L, "balaji@gmail.com", "balaji",null);
        assertThat(customerService.createParkingUser(balaji)).isEqualTo(balaji);
    }
}

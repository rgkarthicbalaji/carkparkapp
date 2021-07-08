package com.asses.park.spring.data.jpa.test;

import com.asses.park.model.Customer;
import com.asses.park.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerJPAUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void should_find_no_customer_if_repository_is_empty() {
        Iterable<Customer> customers = customerRepository.findAll();
        assertThat(customers).isEmpty();
    }

    @Test
    public void should_store_a_customer() {
        Customer customer = customerRepository.save(new Customer(Long.valueOf(1), "karthic@gmail.com", "karthic",null));
        assertThat(customer).hasFieldOrPropertyWithValue("ssNumber", 1L);
        assertThat(customer).hasFieldOrPropertyWithValue("email", "karthic@gmail.com");
        assertThat(customer).hasFieldOrPropertyWithValue("fullName", "karthic");
    }

    @Test
    public void should_find_all_customers() {
        Customer customer1 = new Customer(Long.valueOf(1), "karthic@gmail.com", "karthic",null);
        entityManager.persist(customer1);

        Customer customer2 = new Customer(Long.valueOf(2), "balaji@gmail.com", "balaji",null);
        entityManager.persist(customer2);

        Customer customer3 = new Customer(Long.valueOf(3), "karthicbalaji@gmail.com", "karthicbalaji",null);
        entityManager.persist(customer3);

        Iterable<Customer> customers = customerRepository.findAll();

        assertThat(customers).hasSize(3).contains(customer1, customer2, customer3);
    }

    @Test(expected = NumberFormatException.class)
    public void should_throw_exception_invalid_customer_ss_number() {
        Customer customer = new Customer(Long.parseLong("a"), "karthic@gmail.com", "karthic",null);
        entityManager.persist(customer);
    }

    @Test(expected = PersistenceException.class)
    public void should_throw_exception_for_null_customer_ss_number() {
        Customer customer = new Customer(null, "karthic@gmail.com", "karthic",null);
        entityManager.persist(customer);
    }

    @Test
    public void should_update_customer_by_id() {
        Customer customer1 = new Customer(1L, "karthic@gmail.com", "karthic",null);
        entityManager.persist(customer1);

        Customer customer2 = new Customer(2L, "balaji@gmail.com", "karthic",null);
        entityManager.persist(customer2);

        Customer updatedCustomer = new Customer(3L, "updatedEmail@gmail.com", "updatedName",null);

        Customer customer = customerRepository.findById(customer2.getSsNumber()).get();
        customer.setSsNumber(updatedCustomer.getSsNumber());
        customer.setEmail(updatedCustomer.getEmail());
        customer.setFullName(updatedCustomer.getFullName());
        customerRepository.save(customer);

        Customer checkCustomer = customerRepository.findById(2L).get();

        assertThat(checkCustomer.getSsNumber()).isEqualTo(customer2.getSsNumber());
        assertThat(checkCustomer.getEmail()).isEqualTo(customer2.getEmail());
        assertThat(checkCustomer.getFullName()).isEqualTo(customer2.getFullName());
    }

    @Test
    public void should_delete_customer_by_id() {
        Customer customer1 = new Customer(Long.valueOf(1), "karthic@gmail.com", "karthic",null);
        entityManager.persist(customer1);

        Customer customer2 = new Customer(Long.valueOf(2), "balaji@gmail.com", "balaji",null);
        entityManager.persist(customer2);

        Customer customer3 = new Customer(Long.valueOf(3), "karthicbalaji@gmail.com", "karthicbalaji",null);
        entityManager.persist(customer3);

        customerRepository.deleteById(customer2.getSsNumber());

        Iterable<Customer> customers = customerRepository.findAll();

        assertThat(customers).hasSize(2).contains(customer1, customer3);
    }

    @Test
    public void should_delete_all_customers() {
        entityManager.persist(new Customer(Long.valueOf(1), "karthic@gmail.com", "karthic",null));
        entityManager.persist(new Customer(Long.valueOf(2), "balaji@gmail.com", "balaji",null));

        customerRepository.deleteAll();

        assertThat(customerRepository.findAll()).isEmpty();
    }
}

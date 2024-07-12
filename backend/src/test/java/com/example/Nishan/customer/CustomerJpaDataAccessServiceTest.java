package com.example.Nishan.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class CustomerJpaDataAccessServiceTest {

    private CustomerJpaDataAccessService underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable= MockitoAnnotations.openMocks(this);
        underTest=new CustomerJpaDataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {

        underTest.selectAllCustomers();
        verify(customerRepository)
                .findAll();
    }

    @Test
    void selectCustomerById() {
        int id=1;

        underTest.selectCustomerById(id);

        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        //Given
        Customer customer=new Customer(1,"nishan","girinishan@gmail.com",21, Gender.FEMALE);

        //When
        underTest.insertCustomer(customer);
        //Then

        verify(customerRepository).save(customer);

    }

    @Test
    void existPersonWithEmail() {
        String email="foo@gmail.com";

        underTest.existPersonWithEmail(email);

        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void deleteCustomerById() {

        int id=1;

        underTest.deleteCustomerById(id);

        verify(customerRepository).deleteById(id);
    }

    @Test
    void existPersonWithId() {

        int id=1;

        underTest.existPersonWithId(id);

        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void updateCustomer() {
        //Given
        Customer customer=new Customer(1,"nishan","girinishan@gmail.com",21, Gender.FEMALE);

        //When
        underTest.updateCustomer(customer);
        //Then

        verify(customerRepository).save(customer);
    }
}
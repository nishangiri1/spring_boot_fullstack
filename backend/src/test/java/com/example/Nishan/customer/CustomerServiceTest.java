package com.example.Nishan.customer;

import com.example.Nishan.exception.DuplicateResourceException;
import com.example.Nishan.exception.RequestValidationException;
import com.example.Nishan.exception.ResourcesNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class )
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {

        underTest=new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {

        underTest.getAllCustomers();

        verify(customerDao).selectAllCustomers();

    }

    @Test
    void cangetCustomer() {
        //Given
        int id=10;
        Customer customer=new Customer(id,"nnishan","giri@gmail.com",22, Gender.FEMALE);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        Customer actual = underTest.getCustomer(id);

        assertThat(actual).isEqualTo(customer);

    }

    @Test
    void willThrowWhengetCustomerReturnsEmptyOptional() {
        //Given
        int id=10;

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(()->underTest.getCustomer(id))
                .isInstanceOf(ResourcesNotFoundException.class)
                .hasMessage(
                        "Customer with id [%s] not found".formatted(id)
                );

    }

    @Test
    void addCustomer() {
        String email="alex@gmail.com";
        when(customerDao.existPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest req=new CustomerRegistrationRequest("Nishan",email,34,Gender.FEMALE);
        underTest.addCustomer(req);

        ArgumentCaptor<Customer> customerArgumentCaptor=ArgumentCaptor.forClass(
                Customer.class
        );

        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer=customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(req.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(req.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(req.age());
    }

    @Test
    void willThrowWhenEmailExistWhileAddingCustomer() {
        String email="alex@gmail.com";
        when(customerDao.existPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest req=new CustomerRegistrationRequest("Nishan",email,34,Gender.FEMALE);

        assertThatThrownBy(()->underTest.addCustomer(req))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");



        verify(customerDao,never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        int id=10;

        when(customerDao.existPersonWithId(id)).thenReturn(true);

        underTest.deleteCustomerById(id);

        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willThrowDeleteCustomerByIdNotExist() {
        int id=10;

        when(customerDao.existPersonWithId(id)).thenReturn(false);

        assertThatThrownBy(()->underTest.deleteCustomerById(id))
                .isInstanceOf(ResourcesNotFoundException.class)
                        .hasMessage("Customer with id [%s] not found".formatted(id));

        verify(customerDao,never()).deleteCustomerById(id);
    }

    @Test
    void canupdateAllCustomersProperties() {

        int id=10;
        Customer customer=new Customer(id,"nnishan","giri@gmail.com",22, Gender.FEMALE);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail="girinishan34@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Nishan", newEmail, 21);

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);

        //When
        underTest.updateCustomer(id,updateRequest);

        //Then
        ArgumentCaptor<Customer> argumentCaptor=
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(argumentCaptor.capture());
        Customer capturedCustomer =argumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void canupdateOnlyCustomerName() {

        int id=10;
        Customer customer=new Customer(id,"nnishan","giri@gmail.com",22, Gender.FEMALE);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Nishan", null, null);


        //When
        underTest.updateCustomer(id,updateRequest);

        //Then
        ArgumentCaptor<Customer> argumentCaptor=
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(argumentCaptor.capture());
        Customer capturedCustomer =argumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canupdateOnlyCustomerEmail() {

        int id=10;
        Customer customer=new Customer(id,"nnishan","giri@gmail.com",22, Gender.FEMALE);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail="girinishan34@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, newEmail, null);

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);

        //When
        underTest.updateCustomer(id,updateRequest);

        //Then
        ArgumentCaptor<Customer> argumentCaptor=
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(argumentCaptor.capture());
        Customer capturedCustomer =argumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canupdateOnlyCustomerAge() {

        int id=10;
        Customer customer=new Customer(id,"nnishan","giri@gmail.com",22, Gender.FEMALE);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail="girinishan34@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, 19 );


        //When
        underTest.updateCustomer(id,updateRequest);

        //Then
        ArgumentCaptor<Customer> argumentCaptor=
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(argumentCaptor.capture());
        Customer capturedCustomer =argumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }
    @Test
    void willThrowWhenTryingToUpdateWhenAlreadyTaken() {

        int id=10;
        Customer customer=new Customer(id,"nnishan","giri@gmail.com",22, Gender.FEMALE);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail="girinishan34@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, newEmail, null);

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(true);

        //When
        assertThatThrownBy(()->underTest.updateCustomer(id,updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        //Then

        verify(customerDao,never()).updateCustomer(any());

    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {

        int id=10;
        Customer customer=new Customer(id,"nnishan","giri@gmail.com",22, Gender.FEMALE);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(customer.getName(), customer.getEmail(), customer.getAge());

        //When
        assertThatThrownBy(()->underTest.updateCustomer(id,updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        //Then

        verify(customerDao,never()).updateCustomer(any());

    }
}
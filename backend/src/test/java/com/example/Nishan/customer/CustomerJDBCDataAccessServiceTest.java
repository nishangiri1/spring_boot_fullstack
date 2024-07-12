package com.example.Nishan.customer;

import com.example.Nishan.AbstractTestcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;
import java.util.UUID;


class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper =new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest=new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }


    @Test
    void selectAllCustomers() {
        //Given
        Customer customer= new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID(),
                20,
                Gender.FEMALE);
        underTest.insertCustomer(customer);

        //when
        List<Customer> actual=underTest.selectAllCustomers();

        //Then
        assertThat(actual).isNotEmpty();
    }


    @Test
    void selectCustomerById() {
        //Given
        String email=faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.FEMALE);
        underTest.insertCustomer(customer);
        int id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        Optional<Customer> actual=underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        int id=-1;

        var actual=underTest.selectCustomerById(id);

        assertThat(actual).isEmpty();
    }


    @Test
    void existPersonWithEmail() {
        //Given
        String email=faker.internet().safeEmailAddress()+"-"+UUID.randomUUID();
        String name=faker.name().fullName();
        Customer customer =new Customer(
                name,
                email,
                20,
                Gender.FEMALE);
        underTest.insertCustomer(customer);

        boolean actual=underTest.existPersonWithEmail(email);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnFalseWhenDoesNotExist() {
        //Given
        String email=faker.internet().safeEmailAddress()+"-"+UUID.randomUUID();

        //When
        boolean actual =underTest.existPersonWithEmail(email);

        //Then
        assertThat(actual).isFalse();

    }

    @Test
    void existCustomerWithId() {
        //Given
        String email=faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.FEMALE);
        underTest.insertCustomer(customer);
        int id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var actual=underTest.existPersonWithId(id);

        assertThat(actual).isTrue();
    }

    @Test
    void existPersonWithIdWillReturnFalseWhenIdNotPresent() {
        int id=-1;
        var actual = underTest.existPersonWithId(id);
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById()
    {
        //Given
        String email=faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.FEMALE);

        underTest.insertCustomer(customer);

        int id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        underTest.deleteCustomerById(id);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName()
    {
        //Given
        String email=faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.FEMALE);

        underTest.insertCustomer(customer);

        int id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newName="foo";

        //When age is name
        Customer update=new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c->
         {
            assertThat(c.getId()).isEqualTo(id);
             assertThat(c.getName()).isEqualTo(newName);
             assertThat(c.getEmail()).isEqualTo(customer.getEmail());
             assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }


    @Test
    void updateCustomerEmail()
    {
        //Given
        String email=faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.FEMALE);

        underTest.insertCustomer(customer);

        int id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        var newEmail=faker.internet().safeEmailAddress()+"-"+UUID.randomUUID();

        //When age is name
        Customer update=new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c->
        {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge()
    {
        //Given
        String email=faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.FEMALE);

        underTest.insertCustomer(customer);

        int id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        var newAge=100;

        //When age is name
        Customer update=new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c->
        {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge);
        });
    }
    @Test
    void willUpdateAllPropertiesCustomer()
    {
        //Given
        String email=faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.FEMALE);

        underTest.insertCustomer(customer);

        int id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When age is name
        Customer update=new Customer();
        update.setAge(20);
        update.setName("Nishan");
        update.setId(id);
        update.setEmail(UUID.randomUUID().toString());

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void willNotUpdateWhenNothingToupdate()
    {
        //Given
        String email=faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                faker.name().fullName(),
                email,
                20,
                Gender.FEMALE);

        underTest.insertCustomer(customer);

        int id=underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer update=new Customer();
        update.setId(id);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c->
        {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }


}
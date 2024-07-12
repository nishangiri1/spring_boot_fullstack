package com.example.Nishan.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    private static final List<Customer> customers;

    static {
        customers=  new ArrayList<>();
        Customer alex= new Customer(1,"Nishan","girinishan@gmail.com",21, Gender.FEMALE);
        customers.add(alex);

        Customer jamila=new Customer(2,"Hemanti","girihemanti@gmail.com",20, Gender.FEMALE);
        customers.add(jamila);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(c->c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(c->c.getEmail().equals(email));
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        customers.stream()
                .filter(c->c.getId().equals(customerId))
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public boolean existPersonWithId(Integer id) {
        return customers.stream()
                .anyMatch(c->c.getId().equals(id));
    }

    @Override
    public void updateCustomer(Customer update) {
        customers.add(update);
    }

}

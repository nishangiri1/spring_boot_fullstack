package com.example.Nishan.customer;

import java.util.List;
import java.util.Optional;


public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    public boolean existPersonWithEmail(String email);
    void deleteCustomerById(Integer id);

    boolean existPersonWithId(Integer customerId);

    void updateCustomer(Customer update);
}

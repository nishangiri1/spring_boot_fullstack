package com.example.Nishan.customer;
import com.example.Nishan.exception.DuplicateResourceException;
import com.example.Nishan.exception.RequestValidationException;
import com.example.Nishan.exception.ResourcesNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers()
    {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id)
    {
         return customerDao.selectCustomerById(id).orElseThrow(()-> new ResourcesNotFoundException("Customer with id [%s] not found".formatted(id)) );
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest)
    {
        //check if email exist
        String email=customerRegistrationRequest.email();
        if(customerDao.existPersonWithEmail(customerRegistrationRequest.email()))
        {
            throw new DuplicateResourceException("Email already taken");
        }

        //add
        Customer customer=new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender());

        customerDao.insertCustomer(customer);
    }
    public void deleteCustomerById(Integer customerId)
    {
        if(!customerDao.existPersonWithId(customerId))
        {
            throw new ResourcesNotFoundException(
                    "Customer with id [%s] not found".formatted(customerId)
            );
        }
        customerDao.deleteCustomerById(customerId);
    }

    public void updateCustomer(Integer customerId,CustomerUpdateRequest updateRequest)
    {
        Customer customer= getCustomer(customerId);

        boolean changes=false;

        if(updateRequest.name()!=null && !updateRequest.name().equals(customer.getName()))
        {
            customer.setName(updateRequest.name());
//            customerDao.insertCustomer(customer);
            changes=true;
        }

        if(updateRequest.age()!=null && !updateRequest.age().equals(customer.getAge()))
        {
            customer.setAge(updateRequest.age());
//            customerDao.insertCustomer(customer);
            changes=true;
        }

        if(updateRequest.email()!=null && !updateRequest.email().equals(customer.getEmail()))
        {
            if(customerDao.existPersonWithEmail(updateRequest.email()))
            {
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }

            customer.setEmail(updateRequest.email());
//            customerDao.insertCustomer(customer);
            changes=true;
        }
        if(!changes)
        {
            throw new RequestValidationException("no data changes found");
        }
        customerDao.updateCustomer(customer);

    }

}

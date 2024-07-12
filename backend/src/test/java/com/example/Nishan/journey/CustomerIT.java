package com.example.Nishan.journey;

import com.example.Nishan.customer.Customer;
import com.example.Nishan.customer.CustomerRegistrationRequest;
import com.example.Nishan.customer.CustomerUpdateRequest;
import com.example.Nishan.customer.Gender;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIT {

    private static final Random random=new Random();
    private static final String CUSTOMER_URI="/api/v1/customers";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegesterACustomer() {
        //create regestration request
        Faker faker =new Faker();
        Name fakerName =faker.name();
        String name= fakerName.fullName();
        String email=fakerName.lastName()+ UUID.randomUUID()+"@giri.com";
        int age=random.nextInt(1,90);

        Gender gender=age % 2==0 ? Gender.MALE :Gender.FEMALE;
        CustomerRegistrationRequest customerRegistrationRequest=new CustomerRegistrationRequest(
                name,email,age,gender
        );

        //send post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customers
        List<Customer> allCustomer = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that customer is present
        Customer expectedCustomer =new Customer(name,email,age, gender);

        assertThat(allCustomer).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);


        var id=allCustomer.stream()
                        .filter(customer->customer.getEmail().equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();
        expectedCustomer.setId(id);

        //get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedCustomer);

    }

    @Test
    void canDelete()
    {
        //create regestration request
        Faker faker =new Faker();
        Name fakerName =faker.name();
        String name= fakerName.fullName();
        String email=fakerName.lastName()+ UUID.randomUUID()+"@giri.com";
        int age=random.nextInt(1,90);
        Gender gender=age%2==0?Gender.FEMALE:Gender.MALE;
        CustomerRegistrationRequest customerRegistrationRequest=new CustomerRegistrationRequest(
                name,email,age,gender
        );

        //send post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customers
        List<Customer> allCustomer = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        var id=allCustomer.stream()
                .filter(customer->customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //delete customer
        webTestClient.delete()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        //get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer()
    {
        //create regestration request
        Faker faker =new Faker();
        Name fakerName =faker.name();
        String name= fakerName.fullName();
        String email=fakerName.lastName()+ UUID.randomUUID()+"@giri.com";
        int age=random.nextInt(1,90);
        Gender gender=age%2==0?Gender.FEMALE:Gender.MALE;
        CustomerRegistrationRequest customerRegistrationRequest=new CustomerRegistrationRequest(
                name,email,age,gender
        );

        //send post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customers
        List<Customer> allCustomer = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        var id=allCustomer.stream()
                .filter(customer->customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        String newName = "newName";
        CustomerUpdateRequest request=new CustomerUpdateRequest(newName,null,null);

        //delete customer
        webTestClient.put()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get customer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer expected=new Customer(id,newName,email,age,gender);

        assertThat(updatedCustomer).isEqualTo(expected);
    }
}

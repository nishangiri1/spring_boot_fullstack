package com.example.Nishan.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age,
        Gender gender

)
{

}

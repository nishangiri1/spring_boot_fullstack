package com.example.Nishan.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}

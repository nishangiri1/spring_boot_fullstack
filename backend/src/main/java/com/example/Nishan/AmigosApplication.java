package com.example.Nishan;

import com.example.Nishan.customer.Customer;
import com.example.Nishan.customer.CustomerRepository;
import com.example.Nishan.customer.Gender;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class AmigosApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmigosApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository)
	{


		return args -> {
			var faker=new Faker();
			Random random=new Random();
			int age=random.nextInt(16,99);
			Gender gender= age % 2==0 ? Gender.MALE : Gender.FEMALE;
			Customer customer=new Customer(faker.name().fullName(),faker.internet().safeEmailAddress(), age, gender);
			customerRepository.save(customer);
		};
	}
}

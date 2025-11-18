package com.accenture.franchise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.accenture.franchise")
public class FranchiseApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FranchiseApiApplication.class, args);
	}

}

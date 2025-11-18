package com.accenture.franchise;

import org.springframework.boot.SpringApplication;

public class TestFranchiseApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(FranchiseApiApplication::main)
				.with(ProductServiceTest.class, BranchServiceTest.class, FranchiseServiceTest.class)
				.run(args);
	}

}

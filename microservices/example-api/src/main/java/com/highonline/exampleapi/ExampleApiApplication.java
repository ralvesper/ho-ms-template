package com.highonline.exampleapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.highonline")
public class ExampleApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleApiApplication.class, args);
	}

}

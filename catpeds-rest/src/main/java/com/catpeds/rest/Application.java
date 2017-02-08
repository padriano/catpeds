package com.catpeds.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the standalone Spring Boot application execution.
 *
 * @author padriano
 *
 */
@SpringBootApplication(scanBasePackages = "com.catpeds")
public class Application {

	Application() { }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

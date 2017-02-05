package com.catpeds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the standalone Spring Boot application execution.
 *
 * @author padriano
 *
 */
@SpringBootApplication
public class Application {

	private Application() { }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

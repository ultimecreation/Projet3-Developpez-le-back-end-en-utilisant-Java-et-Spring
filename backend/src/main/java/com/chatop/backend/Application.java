package com.chatop.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
/**
 * @hidden [Application description]
 */
public class Application {

	/**
	 * @param args list of args
	 */
	public static void main(String[] args) {
		Dotenv.configure()
				.load();

		Dotenv.configure()
				.systemProperties()
				.load();
		SpringApplication.run(Application.class, args);
	}

}

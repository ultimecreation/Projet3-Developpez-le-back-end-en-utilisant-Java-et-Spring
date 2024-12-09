package com.chatop.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

@SpringBootApplication
/**
 * @hidden [Application description]
 */
public class Application {

	/**
	 * @param args list of args
	 */
	public static void main(String[] args) {
		Dotenv
				.configure()
				.load();
		// for (DotenvEntry e : dotenv.entries()) {
		// System.out.println("KEY : " + e.getKey());
		// System.out.println("VALUE : " + e.getValue());
		// }
		Dotenv
				.configure()
				.systemProperties()
				.load();
		SpringApplication.run(Application.class, args);
	}

}

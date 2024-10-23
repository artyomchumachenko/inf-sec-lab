package ru.mai.is;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class IsApplication {

	public static void main(String[] args) {
		SpringApplication.run(IsApplication.class, args);
	}

}

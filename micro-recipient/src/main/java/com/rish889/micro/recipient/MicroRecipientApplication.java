package com.rish889.micro.recipient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MicroRecipientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroRecipientApplication.class, args);
	}

}

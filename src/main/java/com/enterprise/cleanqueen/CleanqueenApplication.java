package com.enterprise.cleanqueen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CleanqueenApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleanqueenApplication.class, args);
	}

}

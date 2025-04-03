package com.lhj.fitnessbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FitnessBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitnessBookingApplication.class, args);
	}

}

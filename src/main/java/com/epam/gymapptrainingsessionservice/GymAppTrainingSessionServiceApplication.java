package com.epam.gymapptrainingsessionservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class GymAppTrainingSessionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymAppTrainingSessionServiceApplication.class, args);
		log.info("\n\n ----------- app started -------------\n\n");

	}

}

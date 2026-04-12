package com.example.statement_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import com.example.statement_service.service.StatementService;
@SpringBootApplication
@EnableScheduling
public class StatementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatementServiceApplication.class, args);
	}

//	@Bean
//	CommandLineRunner run(StatementService statementService) {
//		return args -> {
//			// Ici statementService est injecté, donc pas statique
//			statementService.generateStatementsAutomatically();
//			System.out.println("Statements générés !");
//		};
//	}
}

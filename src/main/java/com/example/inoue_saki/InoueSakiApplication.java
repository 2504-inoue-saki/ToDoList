package com.example.inoue_saki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InoueSakiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InoueSakiApplication.class, args);
	}
}

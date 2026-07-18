package com.soup.merlinCastleBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MerlinCastleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MerlinCastleApplication.class, args);
	}

}

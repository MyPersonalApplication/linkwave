package com.example.demo;

import com.example.demo.config.KeycloakConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties(KeycloakConfig.class)
public class ItlinkwaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItlinkwaveApplication.class, args);
	}

}

package com.tesla.health;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableDiscoveryClient
public class CheckHealthAppln1Application {

	public static void main(String[] args) {
		SpringApplication.run(CheckHealthAppln1Application.class, args);
	}

}

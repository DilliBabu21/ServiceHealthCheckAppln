package com.tesla.health.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;

import feign.Feign;

@LoadBalancerClient(name = "serviceHealth-microservice")
public class LoadBalancerConfig {
	@LoadBalanced
	@Bean
	public Feign.Builder feignBuilder() {
		return Feign.builder();
	}
}

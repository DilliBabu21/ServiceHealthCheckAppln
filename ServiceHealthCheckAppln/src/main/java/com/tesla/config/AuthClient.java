package com.tesla.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "AUTH-MICROSERVICE", path="/api/v1/auth")
public interface AuthClient {
	
	 @GetMapping("/authenticate")
	 Authentication authenticate();
}

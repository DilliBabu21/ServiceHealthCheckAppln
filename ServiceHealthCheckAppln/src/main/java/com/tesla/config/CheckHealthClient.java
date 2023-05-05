package com.tesla.config;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tesla.model.Services;


@FeignClient(name = "CHECKHEALTH-MICROSERVICE", path = "/check")
public interface CheckHealthClient {
	
	@GetMapping("/view")
	public List<Services> viewall() ;
}

package com.tesla.health.config;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.tesla.health.model.ServiceModel;

@FeignClient(name = "serviceHealth-microservice", path="/healthcheck")
public interface ServiceHealthClient {

	@GetMapping("/services")
	List<ServiceModel> findAll();

	@PostMapping("/service/add")
	ServiceModel addService(@RequestBody ServiceModel serviceModel);

	@GetMapping("/service/{id}")
	public Optional<ServiceModel> viewService(@PathVariable int id);

	@GetMapping("/service/url")
	public ServiceModel findServiceByUrl(@RequestParam String url);

	@PutMapping("/update")
	public String updateService(@RequestBody ServiceModel serviceModel);
	
	@PutMapping("/update/url")
	public ServiceModel updateService(@RequestBody ServiceModel serviceModel, @RequestParam String url);
	
	@DeleteMapping("/service/{id}")
	public ResponseEntity<?> deleteService(@PathVariable int id);


}

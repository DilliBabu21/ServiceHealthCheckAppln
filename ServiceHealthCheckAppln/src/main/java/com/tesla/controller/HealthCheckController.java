package com.tesla.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.config.CheckHealthClient;
import com.tesla.model.Services;
import com.tesla.security.service.AdminService;
import com.tesla.service.ServicesHealthService;

@RestController
@CrossOrigin("*")
@RequestMapping("/healthcheck")
public class HealthCheckController {

	private ServicesHealthService servicesHealthService;
	private CheckHealthClient checkHealthClient;

	public HealthCheckController(ServicesHealthService servicesHealthService,
			CheckHealthClient checkHealthClient) {
		this.servicesHealthService = servicesHealthService;
		this.checkHealthClient = checkHealthClient;
	}

	@PostMapping("/service/add")
	public Services addService(@RequestBody Services service) {
		return servicesHealthService.addService(service);
	}

	@GetMapping("/service/{id}")
	public Optional<Services> viewService(@PathVariable int id) {
		return servicesHealthService.viewServiceById(id);
	}

	@PostMapping("/service/dummy")
	public ResponseEntity<String> Dummy() {
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/services")
	public List<Services> findAll()  {
		return servicesHealthService.findAllServices();

	}

	@PutMapping("/service/{id}")
	public Services updateService(@RequestBody Services service, @PathVariable int id) {
		return servicesHealthService.updateService(service, id);
	}

	@DeleteMapping("/service/{id}")
	public ResponseEntity<String> deleteService(@PathVariable int id) {
		servicesHealthService.deleteService(id);
		return new ResponseEntity<String>("Service Deleted",HttpStatus.OK);
	}

	@GetMapping("/service/url")
	public ResponseEntity<Services> findServiceByUrl(@RequestParam String url) {
		Services service = servicesHealthService.findServiceByUrl(url);
		if (service == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(service);
	}

	@GetMapping("/view")
	public List<Services> viewall() {
		return checkHealthClient.viewall();

	}

	@PutMapping("/update/url")
	public Services updateService(@RequestBody Services service, @RequestParam String url) {
		return servicesHealthService.updateService(service, url);
	}

}

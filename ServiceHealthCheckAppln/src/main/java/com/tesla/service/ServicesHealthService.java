package com.tesla.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.tesla.config.AuthClient;
import com.tesla.model.Services;
import com.tesla.repo.ServicesHealthRepository;

@Service
@CacheConfig(cacheNames = "services")
public class ServicesHealthService {

	private ServicesHealthRepository servicesHealthRepository;
	private AuthClient authClient;

	

	
	public ServicesHealthService(ServicesHealthRepository servicesHealthRepository, AuthClient authClient) {
		this.servicesHealthRepository = servicesHealthRepository;
		this.authClient = authClient;
	}

	@CacheEvict(allEntries = true)
	public Services addService(Services service) {
		service.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		return servicesHealthRepository.save(service);

	}

	@Cacheable(key = "#id")
	public Optional<Services> viewServiceById(int id) {
		System.out.println("Bye");
		return servicesHealthRepository.findById(id);
	}

	@CacheEvict(allEntries = true)
	public Services updateService(Services service, int id) {
		Services existingService = servicesHealthRepository.findById(id).orElseThrow();
		existingService.setService_name(service.getService_name());
		existingService.setUrl(service.getUrl());
		existingService.setEmail(service.getEmail());
		existingService.setHttpMethod(service.getHttpMethod());
		existingService.setPayload(service.getPayload());
		existingService.setStatus(service.getStatus());
		existingService.setCustomHeaders(service.getCustomHeaders());
		return servicesHealthRepository.save(service);

	}

	@CacheEvict(allEntries = true)
	public ResponseEntity<String> deleteService(int id) {
		servicesHealthRepository.deleteById(id);
		return new ResponseEntity<String>("Service Deleted", HttpStatus.OK);

	}

	@Cacheable
	public List<Services> findAllServices() {

		System.out.println("Hello");
		return servicesHealthRepository.findAll();
		
	}

	public Authentication authenticate() {

		return authClient.authenticate();
	}

	public Services findServiceByUrl(String url) {
		return servicesHealthRepository.findByUrl(url);
	}

	@CacheEvict(allEntries = true)
	public Services updateService(Services service, String url) {
		Services existingService = servicesHealthRepository.findByUrl(url);
		existingService.setStatus(service.getStatus());
		existingService.setCheckedAt(service.getCheckedAt());
		existingService.setResponseCode(service.getResponseCode());
		existingService.setResponseMessage(service.getResponseCode());
		servicesHealthRepository.save(existingService);
		return existingService;

	}

}

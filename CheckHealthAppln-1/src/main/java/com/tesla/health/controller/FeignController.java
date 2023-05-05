package com.tesla.health.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.health.config.ServiceHealthClient;
import com.tesla.health.model.ServiceModel;
import com.tesla.health.service.CheckHealthService;

@RestController
@RequestMapping("/check")
@CrossOrigin("*")
public class FeignController {

	@Autowired
	private ServiceHealthClient serviceHealthClient;
	@Autowired
	private CheckHealthService checkHealthService;

	@GetMapping("/services")
	public List<ServiceModel> findAll() {
		return checkHealthService.findAllServices ();

	}

	@GetMapping("/service/check/{id}")
	public ServiceModel checkHealthById(@PathVariable int id) throws Exception {
		return checkHealthService.checkHealthById(id);

	}
	@GetMapping("/service/{id}")
	public Optional<ServiceModel> viewById(@PathVariable int id) throws Exception {
		return serviceHealthClient.viewService(id);

	}
	@GetMapping("/service")
	public ServiceModel viewByuel(@RequestParam String url){
		return serviceHealthClient.findServiceByUrl(url);

	}
//	@GetMapping("/view")
//	public List<Services> viewall() {
//		return checkHealthService.viewAll();
//
//	}

	@GetMapping("/service/checkall")
	public String checkAllServices() {
		return checkHealthService.checkAllServices();

	}

}

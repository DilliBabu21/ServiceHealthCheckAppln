package com.tesla.health.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tesla.health.model.ServiceModel;
import com.tesla.health.model.Services;

public interface FeignRepo extends JpaRepository<Services, Integer>{
	
//	Services findByUrl(String url);

}

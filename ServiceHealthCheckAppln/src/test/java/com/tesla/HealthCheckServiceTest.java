package com.tesla;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.tesla.model.ServiceStatus;
import com.tesla.model.Services;
import com.tesla.repo.ServicesHealthRepository;

import feign.Request.HttpMethod;



@RunWith(MockitoJUnitRunner.class)
public class HealthCheckServiceTest {

	@Mock
	private ServicesHealthRepository servicesHealthRepository;

	@Mock
	private RestTemplate restTemplate;


	private Services service1;

	@BeforeEach
	public void setup() {
		service1 = new Services();
		service1.setService_name("Service 1");
		service1.setUrl("http://localhost:8080/service1");
		service1.setEmail("exampleaaaaaaaaa2gmail.com");
		service1.setHttpMethod(HttpMethod.GET.toString());
		service1.setPayload("");
		service1.setCustomHeaders(new ArrayList<>());
		service1.setStatus(ServiceStatus.UNKNOWN);
	}
}
	
package com.tesla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tesla.config.AuthClient;
import com.tesla.model.ServiceStatus;
import com.tesla.model.Services;
import com.tesla.repo.ServicesHealthRepository;
import com.tesla.service.ServicesHealthService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceHealthServiceTest {
	private ServicesHealthService servicesHealthService;
	private Services savedService;

	@Autowired
	private ServicesHealthRepository servicesHealthRepository;

	@Autowired
	private AuthClient authClient;

	@BeforeEach
	public void setup() {
//		servicesHealthService = new ServicesHealthService(servicesHealthRepository, authClient);

		Services service = new Services();
		service.setService_name("Test Service");
		service.setUrl("https://test-service.com");
		service.setEmail("test@test-service.com");
		service.setHttpMethod("GET");
		service.setPayload("");
		service.setStatus(ServiceStatus.UNKNOWN);

		savedService = servicesHealthService.addService(service);
	}

	@AfterEach
	public void cleanup() {
		servicesHealthService.deleteService(savedService.getId());
	}
//	  @Test
//	    public void testFindAllServices() {
//	        List<Services> servicesList = servicesHealthService.findAllServices();
//
//	        assertNotNull(servicesList);
//	        assertFalse(servicesList.isEmpty());
//	        assertEquals(3, servicesList.size());
//
//	        Services retrievedService = servicesList.get(0);
//
//	        assertNotNull(retrievedService);
//	        assertEquals(savedService.getId(), retrievedService.getId());
//	        assertEquals(savedService.getService_name(), retrievedService.getService_name());
//	        assertEquals(savedService.getUrl(), retrievedService.getUrl());
//	        assertEquals(savedService.getEmail(), retrievedService.getEmail());
//	        assertEquals(savedService.getHttpMethod(), retrievedService.getHttpMethod());
//	        assertEquals(savedService.getPayload(), retrievedService.getPayload());
//	        assertEquals(savedService.getStatus(), retrievedService.getStatus());
//	    }
	@Test
	public void testAddService() {
		assertNotNull(savedService.getId());
		assertEquals("Test Service", savedService.getService_name());
		assertEquals("https://test-service.com", savedService.getUrl());
		assertEquals("test@test-service.com", savedService.getEmail());
		assertEquals("GET", savedService.getHttpMethod());
		assertEquals("", savedService.getPayload());
		assertEquals(ServiceStatus.UNKNOWN, savedService.getStatus());
	}

//	@Test
//	public void testViewServiceById() {
//		Optional<Services> retrievedService = servicesHealthService.viewServiceById(savedService.getId());
//
//		assertNotNull(retrievedService);
//		assertEquals(savedService.getId(), retrievedService.getId());
//		assertEquals(savedService.getService_name(), retrievedService.getService_name());
//		assertEquals(savedService.getUrl(), retrievedService.getUrl());
//		assertEquals(savedService.getEmail(), retrievedService.getEmail());
//		assertEquals(savedService.getHttpMethod(), retrievedService.getHttpMethod());
//		assertEquals(savedService.getPayload(), retrievedService.getPayload());
//		assertEquals(savedService.getStatus(), retrievedService.getStatus());
//	}

	@Test
	public void testUpdateService() {
		Services updatedService = new Services();
		updatedService.setService_name("Updated Test Service");
		updatedService.setUrl("https://updated-test-service.com");
		updatedService.setEmail("test@updated-test-service.com");
		updatedService.setHttpMethod("POST");
		updatedService.setPayload("test-payload");
		updatedService.setStatus(ServiceStatus.UNKNOWN);

//		String message = servicesHealthService.updateService(updatedService, savedService.getId());

//		assertEquals("Updated Successfully", message);

//		Services retrievedService = servicesHealthService.viewServiceById(savedService.getId());
//
//		assertNotNull(retrievedService);
//		assertEquals(savedService.getId(), retrievedService.getId());
//		assertEquals(updatedService.getService_name(), retrievedService.getService_name());
//		assertEquals(updatedService.getUrl(), retrievedService.getUrl());
//		assertEquals(updatedService.getEmail(), retrievedService.getEmail());
//		assertEquals(updatedService.getHttpMethod(), retrievedService.getHttpMethod());
//		assertEquals(updatedService.getPayload(), retrievedService.getPayload());
//		assertEquals(updatedService.getStatus(), retrievedService.getStatus());
//	}

//	@Test
//	public void testDeleteService() {
//	    String message = servicesHealthService.deleteService(savedService.getId());
//
//	    assertEquals("Service Deleted", message);
//
//	    // Try to retrieve the service by its ID and check that it's null
//	    Services deletedService = servicesHealthService.viewServiceById(savedService.getId());
//	    assertNull(deletedService);
//	}

	  

	}
}

package com.tesla.health.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.tesla.health.config.ServiceHealthClient;
import com.tesla.health.model.Header;
import com.tesla.health.model.ServiceModel;
import com.tesla.health.model.ServiceStatus;
import com.tesla.health.model.Services;
import com.tesla.health.repo.FeignRepo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CheckHealthService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ServiceHealthClient feignRepo;

	@Autowired
	private FeignRepo repository;

	@Autowired
	private FallbackMethods fallbackMethods;

	private final String FindAllService = "findAllServices";

	/**
	 * It prepares the HTTP request entity for the given service by setting the
	 * headers and payload.
	 * 
	 * @param service the service to prepare the request for
	 * @return the HTTP request entity with the headers and payload set
	 */
	private HttpEntity<String> prepareRequest(ServiceModel service) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		List<Header> customHeaders = service.getCustomHeaders();
		for (Header header : customHeaders) {
			headers.set(header.getKey(), header.getValue());
		}

		Object payload = service.getPayload();
		if (payload instanceof String) {
			String payloadStr = (String) payload;
			try {
				payload = Integer.parseInt(payloadStr);
			} catch (NumberFormatException e) {
				try {
					payload = Long.parseLong(payloadStr);
				} catch (NumberFormatException ex) {
					try {
						payload = Double.parseDouble(payloadStr);
					} catch (NumberFormatException exc) {
						// if payload is not numeric, do nothing
					}
				}
			}
		}

		return new HttpEntity<String>((String) payload, headers);
	}

	/**
	 * Sends an HTTP request to the specified service URL using the specified HTTP
	 * method and request entity.
	 * 
	 * @param service       The service to send the request
	 * @param requestEntity The HTTP request entity containing the request payload
	 *                      and headers
	 * @return A ResponseEntity<String> object representing the HTTP response
	 *         received from the service
	 */
	private ResponseEntity<String> sendRequest(ServiceModel service, HttpEntity<String> requestEntity) {
		String httpMethod = service.getHttpMethod();
		if (httpMethod.equals("POST")) {
			return restTemplate.exchange(service.getUrl(), HttpMethod.POST, requestEntity, String.class);
		} else if (httpMethod.equals("PUT")) {
			return restTemplate.exchange(service.getUrl(), HttpMethod.PUT, requestEntity, String.class);
		} else if (httpMethod.equals("DELETE")) {
			return restTemplate.exchange(service.getUrl(), HttpMethod.DELETE, requestEntity, String.class);
		} else {
			return restTemplate.exchange(service.getUrl(), HttpMethod.GET, requestEntity, String.class);
		}
	}

	/**
	 * This method checks the health of a service by sending a request to the
	 * specified URL using the provided HTTP method and payload, with optional
	 * custom headers. If the service already exists in the checkHealth database,
	 * its status is updated with the results of the health check, including
	 * response code and message, if any.
	 * 
	 * @param service       The service to be checked
	 * @param url           The URL of the service to be checked.
	 * @param httpMethod    The HTTP method to be used for the health check like
	 *                      GET, POST, PUT, DELETE.
	 * @param payload       The payload to be sent with the health check request
	 * @param customHeaders A list of custom headers to be sent with the health
	 *                      check request.
	 * @return The existing service in the checkHealth database with updated status
	 *         and response information, or a new service if one was created
	 */
	@CircuitBreaker(name = "CheckHealth", fallbackMethod = "checkHealthFallback")
	public ServiceModel checkHealth(ServiceModel service, String url, String httpMethod, Object payload,
			List<Header> customHeaders) {
		ServiceModel existingService;

		existingService = feignRepo.findServiceByUrl(url);

		long startTime = System.currentTimeMillis();

		try {
			HttpEntity<String> requestEntity = prepareRequest(service);
			ResponseEntity<String> response = sendRequest(service, requestEntity);

			long endTime = System.currentTimeMillis();
			long timeTaken = endTime - startTime;

			if (response.getStatusCode().is2xxSuccessful()) {
				if (existingService != null && timeTaken > 10000) { // 10 seconds
					existingService.setStatus(ServiceStatus.SLOW);
					service.setResponseCode(response.getStatusCode().toString());
				} else {
					existingService.setStatus(ServiceStatus.UP);
					service.setResponseCode(response.getStatusCode().toString());
				}
			} else if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
				existingService.setStatus(ServiceStatus.ERROR);
				existingService.setResponseCode(response.getStatusCode().toString());

			} else {
				existingService.setStatus(ServiceStatus.DOWN);
				existingService.setResponseCode(response.getStatusCode().toString());
				int maxLength = 10000; // Or any other appropriate value
				if (response.getBody().length() > maxLength) {
					existingService.setResponseMessage(response.getBody().substring(0, maxLength));
				} else {
					existingService.setResponseMessage(response.getBody());
				}
			}
			existingService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
			feignRepo.updateService(existingService, url);
			return existingService;
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			if (existingService != null) {
				existingService.setStatus(ServiceStatus.ERROR);
				existingService.setResponseCode(ex.getStatusCode().toString());
				existingService.setCheckedAt(new Timestamp(System.currentTimeMillis()));

				feignRepo.updateService(existingService, url);
				return existingService;
			} else {
				service.setStatus(ServiceStatus.ERROR);
				service.setResponseCode(ex.getStatusCode().toString());
				service.setCheckedAt(new Timestamp(System.currentTimeMillis()));
				feignRepo.updateService(service, url);
				return service;
			}
		} catch (Exception ex) {
			if (existingService != null) {
				existingService.setStatus(ServiceStatus.DOWN);

				int maxLength = 10000;
				if (ex.getMessage().length() > maxLength) {
					existingService.setResponseMessage(ex.getMessage().substring(0, maxLength));
				} else {
					existingService.setResponseMessage(ex.getMessage());
				}

				existingService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
				feignRepo.updateService(existingService, url);
				return existingService;
			} else {
				service.setStatus(ServiceStatus.DOWN);
				int maxLength = 10000;
				if (ex.getMessage().length() > maxLength) {
					service.setResponseMessage(ex.getMessage().substring(0));
				} else {
					service.setResponseMessage(ex.getMessage());
				}
				service.setCheckedAt(new Timestamp(System.currentTimeMillis()));
				return feignRepo.updateService(service, url);

			}
		}

	}

//	public Services checkHealthFallback(Services service, String url, String httpMethod, Object payload,
//			List<Header> customHeaders, Throwable t) throws Exception {
//		return fallbackMethods.checkHealthFallback(service, url, httpMethod, payload, customHeaders, t);
//}

	/**
	 * This method checks the health of a service by id.
	 * 
	 * @param id the id of the service to check.
	 * @return updated Services object if the service is present and healthy.
	 * @throws Exception if the service is not found with the given id.
	 */
	@CircuitBreaker(name = "CheckHealthById", fallbackMethod = "checkHealthByIdFallBack")
	public ServiceModel checkHealthById(int id) throws Exception {
		Optional<ServiceModel> serviceOptional = feignRepo.viewService(id);
		if (serviceOptional.isPresent()) {
			ServiceModel service = serviceOptional.get();
			String url = service.getUrl();
			String httpMethod = service.getHttpMethod();
			Object payload = service.getPayload();
			List<Header> headers = service.getCustomHeaders();

			ServiceModel updatedService = checkHealth(service, url, httpMethod, payload, headers);
			feignRepo.updateService(updatedService, url);
			return updatedService;

		} else {
			throw new Exception("Service not found with id " + id);
		}
	}

	public ServiceModel checkHealthByIdFallBack(int id, Throwable t) throws Exception {
		ServiceModel service = new ServiceModel();
		if (service.getId() == 0) {
			service.setFallbackMessage("Service Unavailable");
			return service;
		} else {
			throw new Exception("Service not found with id " + id);
		}
	}

	/**
	 * This method runs a health check for all services at a fixed interval using a
	 * scheduled task. The health check is performed every one hour. If the HTTP
	 * method of a service is POST or GET, the method performs the health check
	 * immediately. If the HTTP method is PUT or DELETE, the method waits for 2 or 3
	 * seconds, respectively, before performing the health check. The task is
	 * submitted to an executor service to run asynchronously to check all the
	 * services.
	 */
	@Scheduled(fixedRate = 3600 * 10000) // run every one hour
	public void scheduleHealthCheck() {
		List<ServiceModel> servicesList = feignRepo.findAll();
		ExecutorService executor = Executors.newCachedThreadPool();
		for (ServiceModel service : servicesList) {
			executor.submit(() -> {
				String url = service.getUrl();
				Object payload = service.getPayload();
				List<Header> header = service.getCustomHeaders();
				String httpMethod = service.getHttpMethod();
				if (httpMethod.equalsIgnoreCase("POST") || httpMethod.equalsIgnoreCase("GET")) {
					ServiceModel updatedService = checkHealth(service, url, httpMethod, payload, header);
					updatedService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
					System.out.println(checkHealth(service, url, httpMethod, payload, header));
					feignRepo.updateService(updatedService, url);
				} else if (httpMethod.equalsIgnoreCase("PUT")) {
					try {
						Thread.sleep(2000); // wait 2 seconds before checking PUT requests
						ServiceModel updatedService = checkHealth(service, url, httpMethod, payload, header);
						updatedService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
						feignRepo.updateService(updatedService, url);
					} catch (InterruptedException e) {
						// log or handle the exception
					}
				} else if (httpMethod.equalsIgnoreCase("DELETE")) {
					try {
						Thread.sleep(3000); // wait 3 seconds before checking DELETE requests
						ServiceModel updatedService = checkHealth(service, url, httpMethod, payload, header);
						updatedService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
						feignRepo.updateService(updatedService, url);
					} catch (InterruptedException e) {
						// log or handle the exception
					}
				}
			});
		}
		executor.shutdown();
	}

	/**
	 * This method checks the health of all the services in the repository by
	 * submitting each service to an executor to run in a separate thread.
	 *
	 * It retrieves the list of services from the repository and then for each
	 * service, it creates an HttpEntity, sends a request to the service using the
	 * checkHealth() method, and saves the result back to the repository.
	 * 
	 * @return a message indicating that all services have been checked.
	 */
	@CircuitBreaker(name = "checkAllServices", fallbackMethod = "checkAllServicesFallback")
	public String checkAllServices() {
		List<ServiceModel> servicesList = feignRepo.findAll();
		ExecutorService executor = Executors.newCachedThreadPool(); // define executor service
		for (ServiceModel service : servicesList) {
			executor.submit(() -> { // submit task to executor
				String url = service.getUrl();
				Object payload = service.getPayload();
				List<Header> header = service.getCustomHeaders();
				String httpMethod = service.getHttpMethod();
				if (httpMethod.equalsIgnoreCase("POST") || httpMethod.equalsIgnoreCase("GET")) {
					ServiceModel updatedService = checkHealth(service, url, httpMethod, payload, header);
					updatedService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
					System.out.println(checkHealth(service, url, httpMethod, payload, header));
					feignRepo.updateService(updatedService, url);
				} else if (httpMethod.equalsIgnoreCase("PUT")) {
					try {
						Thread.sleep(2000); // wait 2 seconds before checking PUT requests
						ServiceModel updatedService = checkHealth(service, url, httpMethod, payload, header);
						updatedService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
						feignRepo.updateService(updatedService, url);
					} catch (InterruptedException e) {
						// log or handle the exception
					}
				} else if (httpMethod.equalsIgnoreCase("DELETE")) {
					try {
						Thread.sleep(3000); // wait 3 seconds before checking DELETE requests
						ServiceModel updatedService = checkHealth(service, url, httpMethod, payload, header);
						updatedService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
						feignRepo.updateService(updatedService, url);
					} catch (InterruptedException e) {
						// log or handle the exception
					}
				}
			});
		}
		executor.shutdown();
		return "Checked All Services";
	}

	public String checkAllServicesFallback(Throwable t) {
		
		return "Service unavailable";

	}

	/**
	 * 
	 * @return
	 */
	@CircuitBreaker(name = "FindAllService", fallbackMethod = "findAllServicesFallback")
	public List<ServiceModel> findAllServices() {
		return feignRepo.findAll();
	}
	public List<Services> findAllServicesFallback(Throwable t) {
		return repository.findAll();

	}

}

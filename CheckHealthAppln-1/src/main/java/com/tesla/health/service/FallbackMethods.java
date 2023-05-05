package com.tesla.health.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.tesla.health.model.Header;
import com.tesla.health.model.ServiceStatus;
import com.tesla.health.model.ServiceModel;
import com.tesla.health.repo.FeignRepo;

@Service
public class FallbackMethods {
	
//	@Autowired
//	private RestTemplate restTemplate;
//
////	@Autowired
////	private FeignRepo repository;
//	
//	/**
//	 * It prepares the HTTP request entity for the given service by setting the
//	 * headers and payload.
//	 * 
//	 * @param service the service to prepare the request for
//	 * @return the HTTP request entity with the headers and payload set
//	 */
//	private HttpEntity<String> prepareRequest(Service service) {
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		List<Header> customHeaders = service.getCustomHeaders();
//		for (Header header : customHeaders) {
//			headers.set(header.getKey(), header.getValue());
//		}
//
//		Object payload = service.getPayload();
//		if (payload instanceof String) {
//			String payloadStr = (String) payload;
//			try {
//				payload = Integer.parseInt(payloadStr);
//			} catch (NumberFormatException e) {
//				try {
//					payload = Long.parseLong(payloadStr);
//				} catch (NumberFormatException ex) {
//					try {
//						payload = Double.parseDouble(payloadStr);
//					} catch (NumberFormatException exc) {
//						// if payload is not numeric, do nothing
//					}
//				}
//			}
//		}
//
//		return new HttpEntity<String>((String) payload, headers);
//	}
//
//	/**
//	 * Sends an HTTP request to the specified service URL using the specified HTTP
//	 * method and request entity.
//	 * 
//	 * @param service       The service to send the request
//	 * @param requestEntity The HTTP request entity containing the request payload
//	 *                      and headers
//	 * @return A ResponseEntity<String> object representing the HTTP response
//	 *         received from the service
//	 */
//	private ResponseEntity<String> sendRequest(Service service, HttpEntity<String> requestEntity) {
//		String httpMethod = service.getHttpMethod();
//		if (httpMethod.equals("POST")) {
//			return restTemplate.exchange(service.getUrl(), HttpMethod.POST, requestEntity, String.class);
//		} else if (httpMethod.equals("PUT")) {
//			return restTemplate.exchange(service.getUrl(), HttpMethod.PUT, requestEntity, String.class);
//		} else if (httpMethod.equals("DELETE")) {
//			return restTemplate.exchange(service.getUrl(), HttpMethod.DELETE, requestEntity, String.class);
//		} else {
//			return restTemplate.exchange(service.getUrl(), HttpMethod.GET, requestEntity, String.class);
//		}
//	}
	
	
//	public Services checkHealthFallback(Services service, String url, String httpMethod, Object payload,
//			List<Header> customHeaders, Throwable t) throws Exception {
//		Services existingService;
//
//		existingService = fe.findByUrl(url);
//
//		long startTime = System.currentTimeMillis();
//
//		try {
//			HttpEntity<String> requestEntity = prepareRequest(service);
//			ResponseEntity<String> response = sendRequest(service, requestEntity);
//
//			long endTime = System.currentTimeMillis();
//			long timeTaken = endTime - startTime;
//
//			if (response.getStatusCode().is2xxSuccessful()) {
//				if (existingService != null && timeTaken > 10000) { // 10 seconds
//					existingService.setStatus(ServiceStatus.SLOW);
//					service.setResponseCode(response.getStatusCode().toString());
//				} else {
//					existingService.setStatus(ServiceStatus.UP);
//					service.setResponseCode(response.getStatusCode().toString());
//				}
//			} else if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
//				existingService.setStatus(ServiceStatus.ERROR);
//				existingService.setResponseCode(response.getStatusCode().toString());
//
//			} else {
//				existingService.setStatus(ServiceStatus.DOWN);
//				existingService.setResponseCode(response.getStatusCode().toString());
//				int maxLength = 10000; // Or any other appropriate value
//				if (response.getBody().length() > maxLength) {
//					existingService.setResponseMessage(response.getBody().substring(0, maxLength));
//				} else {
//					existingService.setResponseMessage(response.getBody());
//				}
//			}
//			existingService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
//			return existingService;
//		
//	} catch (HttpClientErrorException | HttpServerErrorException ex) {
//		if (existingService != null) {
//			existingService.setStatus(ServiceStatus.ERROR);
//			existingService.setResponseCode(ex.getStatusCode().toString());
//			existingService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
//
//			return existingService;
//		} else {
//			service.setStatus(ServiceStatus.ERROR);
//			service.setResponseCode(ex.getStatusCode().toString());
//			service.setCheckedAt(new Timestamp(System.currentTimeMillis()));
//			return service;
//		}
//	} catch (Exception ex) {
//		if (existingService != null) {
//			existingService.setStatus(ServiceStatus.DOWN);
//
//			int maxLength = 10000;
//			if (ex.getMessage().length() > maxLength) {
//				existingService.setResponseMessage(ex.getMessage().substring(0, maxLength));
//			} else {
//				existingService.setResponseMessage(ex.getMessage());
//			}
//
//			existingService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
//			return existingService;
//		} else {
//			service.setStatus(ServiceStatus.DOWN);
//			int maxLength = 10000;
//			if (ex.getMessage().length() > maxLength) {
//				service.setResponseMessage(ex.getMessage().substring(0));
//			} else {
//				service.setResponseMessage(ex.getMessage());
//			}
//			service.setCheckedAt(new Timestamp(System.currentTimeMillis()));
//			return service;
//		}
//	}
//
//}
//	
//	public Services checkHealthByIdFallBack(int id, Throwable t) throws Exception {
//		Optional<Services> serviceOptional = repository.findById(id);
//		if (serviceOptional.isPresent()) {
//			Services service = serviceOptional.get();
//			String url = service.getUrl();
//			String httpMethod = service.getHttpMethod();
//			Object payload = service.getPayload();
//			List<Header> headers = service.getCustomHeaders();
//
//			Services updatedService = checkHealthFallback(service, url, httpMethod, payload, headers, t);
////			repository.save(updatedService);
//			return updatedService;
//
//		} else {
//			throw new Exception("Service not found with id " + id);
//		}
//	}
//	
//	public List<Services> findAllServicesFallback(Throwable t) {
//		List<Services> services = repository.findAll();	
//		return services;
//	}


}

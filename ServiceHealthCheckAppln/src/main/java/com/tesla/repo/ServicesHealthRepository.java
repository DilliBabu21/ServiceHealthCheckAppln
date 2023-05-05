package com.tesla.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tesla.model.Services;

@Repository
public interface ServicesHealthRepository extends JpaRepository<Services, Integer>{

//	public Optional<Services> findServicesByUrl(String url);
	public Services findByUrl(String url);


}

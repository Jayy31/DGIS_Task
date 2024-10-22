package com.customer.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.customer.model.State;


public interface StateRepository extends JpaRepository<State, Long> {
	
}
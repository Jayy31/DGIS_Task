package com.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.customer.model.Customer;
import com.customer.model.Identity;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	 List<Customer> findByIdentity(Identity identity);
}
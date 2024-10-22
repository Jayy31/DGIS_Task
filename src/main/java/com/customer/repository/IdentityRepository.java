package com.customer.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.customer.model.Identity;

@Repository
public interface IdentityRepository extends JpaRepository<Identity, Long> {
    Identity findByUsername(String username);
}

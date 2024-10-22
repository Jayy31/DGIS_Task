package com.customer.service;


import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.customer.repository.IdentityRepository;
import com.customer.model.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private IdentityRepository identityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user by username using the repository
        Identity identity = identityRepository.findByUsername(username);
        
        // Check if identity is null
        if (identity == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        
        // Return CustomUserDetails with the identity's details
        return new CustomUserDetails(identity.getUsername(), identity.getPassword(), authorities());
    }

    // Define authorities for the user
    public Collection<? extends GrantedAuthority> authorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")); // Adjust role as needed
    }
}
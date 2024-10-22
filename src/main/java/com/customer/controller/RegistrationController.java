package com.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import com.customer.model.Identity;
import com.customer.repository.IdentityRepository;
import jakarta.validation.*;

@Controller
@RequestMapping("/user")
public class RegistrationController {
	
    @Autowired
    private IdentityRepository identityRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    
    @Autowired
    private CustomerController customerController;
    

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("identity", new Identity()); // Add an empty Identity object
        return "login"; // Make sure this returns the correct login view
    }


    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        // Add an empty Identity object to the model so the form can bind to it
        model.addAttribute("identity", new Identity());
        return "register";
    }

    // Registration
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("identity") Identity identity, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register"; // If there are validation errors
        }
        
        // Hash the password before saving
        String encodedPassword = passwordEncoder.encode(identity.getPassword());
        identity.setPassword(encodedPassword);

        // Save the user to the database using the repository
        identityRepository.save(identity);
        return "redirect:/user/login"; // Redirect to login after successful registration
    }

 // Login
    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username, 
                            @RequestParam("password") String password, Model model) {
        // Find user by username using the repository
        Identity identity = identityRepository.findByUsername(username);

        if (identity != null) {
            // Check if the password matches using the password encoder
            if (passwordEncoder.matches(password, identity.getPassword())) {
                // Authentication successful, redirect to customers list
                return "redirect:/customers/list";
            }
        }

        // If login fails
        model.addAttribute("error", "Invalid username or password.");
        return "login"; // Return login view
    }


}

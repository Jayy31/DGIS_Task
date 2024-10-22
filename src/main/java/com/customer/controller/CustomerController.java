package com.customer.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.customer.model.Customer;
import com.customer.model.District;
import com.customer.repository.*;
import com.customer.model.*;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private DistrictRepository districtRepository;
    
    @Autowired
    private IdentityRepository identityRepository; // To fetch the logged-in user

    @GetMapping("/list")
    public String listCustomers(Model model, Principal principal) {
    	// Get the currently logged-in user's username
        String username = principal.getName();
        Identity identity = identityRepository.findByUsername(username);
        String domainName = identity != null ? identity	.getDomainName() : "";
        
        // Fetch customers only for the logged-in user
        model.addAttribute("username", username);
        model.addAttribute("domainName", domainName);
        model.addAttribute("customers", customerRepository.findByIdentity(identity));
        model.addAttribute("states", stateRepository.findAll());
        model.addAttribute("customer", new Customer()); // Add an empty Customer object
        return "list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("states", stateRepository.findAll());
        return "add";
    }

    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute Customer customer, Principal principal) {
    	
    	String username = principal.getName();
        Identity identity = identityRepository.findByUsername(username);
        
     // Set the identity for the new customer
        customer.setIdentity(identity);
    	
        if (customer.getDistrict() != null) {
            District district = districtRepository.findById(customer.getDistrict().getId()).orElse(null);
            customer.setDistrict(district);
        }
        customerRepository.save(customer);
        return "redirect:/customers/list";
    }


    @GetMapping("/edit/{id}")
    public String editCustomer(@PathVariable Long id, Model model) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("customer", customer);
        model.addAttribute("states", stateRepository.findAll());
        return "list"; // Return to the same list page
    }
    
    @PostMapping("/update")
    public String updateCustomer(@ModelAttribute Customer customer, Principal principal) {
    	// Find the user by their username (which is stored in the Principal object)
        String username = principal.getName();
        Identity identity = identityRepository.findByUsername(username);
        
        if (customer.getDistrict() != null) {
            District district = districtRepository.findById(customer.getDistrict().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid district Id:" + customer.getDistrict().getId()));
            
            // Set the district and its state to the customer
            customer.setDistrict(district);
        }
        
        // Set the user who is updating the customer
        customer.setIdentity(identity);  // Ensure that you have a setUser method in your Customer entity

        customerRepository.save(customer);
        return "redirect:/customers/list";
    }


//    @GetMapping("/delete/{id}")
//    public String deleteCustomer(@PathVariable("id") Long id) {
//        customerRepository.deleteById(id);
//        return "redirect:/customers/list";
//    }
    
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") Long id) {
        try {
            // Check if customer exists
            Optional<Customer> customer = customerRepository.findById(id);
            if (!customer.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
            }

            // Proceed to delete customer
            customerRepository.deleteById(id);
            return ResponseEntity.ok("Customer deleted successfully");
        } catch (Exception e) {
            // Log the exception and return a generic error message
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting customer");
        }
    }

    @GetMapping("/districts/{stateId}")
    @ResponseBody
    public List<District> getDistrictsByState(@PathVariable Long stateId) {
        return districtRepository.findByStateId(stateId);
    }

}

package org.uwindsor.comp8117.cssjava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uwindsor.comp8117.cssjava.dto.Customer;
import org.uwindsor.comp8117.cssjava.dto.LoginRequest;
import org.uwindsor.comp8117.cssjava.service.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/login")
    public ResponseEntity<Customer> login(@RequestBody LoginRequest request) {
        Customer customer = customerService.login(request.getUsername(), request.getPassword());
        if (customer != null) {
            customer.setPassword(null);
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

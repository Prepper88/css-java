package org.uwindsor.comp8117.cssjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.repository.CustomerRepository;
import org.uwindsor.comp8117.cssjava.dto.Customer;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer login(String username, String password) {
        return customerRepository.findByUsernameAndPassword(username, password).orElse(null);
    }
}
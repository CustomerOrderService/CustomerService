package com.example.customerservice.controller;

import com.example.customerservice.entity.Customer;
import com.example.customerservice.service.CustomerService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/customers")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Customer> getAllCustomer(){
        return customerService.getAllCustomer();
    }
    @PostMapping("/")
    public Customer saveCustomer(@RequestBody Customer customer){
        return customerService.saveCustomer(customer);
    }
    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") Long customerId){
        customerService.deleteCustomer(customerId);
    }

    @GetMapping("/{id}")
    @Cacheable("customer")
    public Customer findCustomerById(@PathVariable("id") Long customerId){
        Customer c = null;
            c = customerService.findCustomerById(customerId);
        return c;
    }

}

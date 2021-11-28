package com.example.customerservice.service;

import com.example.customerservice.entity.Customer;
import com.example.customerservice.repository.CustomerRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }


    @Retry(name = "retryService", fallbackMethod = "localCacheCustomerSearch")
    public Customer findCustomerById(Long id){
        System.out.println("Find by id from DB");
        return customerRepository.findById(id).get();
    }
    private Customer localCacheCustomerSearch(Long id, RuntimeException re) {
        System.out.println("Returning search results from cache");
        return customerRepository.findById(id).get();
    }

    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
    @CacheEvict("customer")
    public void clearCacheById(int id) {
    }
    @CacheEvict(value = "customer", allEntries = true)
    public void clearCache() {
    }
}

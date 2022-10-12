package com.nttdata.bootcamp.mscustomers.interfaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.bootcamp.mscustomers.infraestructure.ICustomerRepository;
import com.nttdata.bootcamp.mscustomers.model.Customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements ICustomerService {
    @Autowired
    private ICustomerRepository repository;

    @Override
    public Mono<Customer> createCustomer(Mono<Customer> customer) {
        return customer.flatMap(repository::insert);
    }

    @Override
    public Flux<Customer> findAllCustomers() {
        return repository.findAll();
    }

}

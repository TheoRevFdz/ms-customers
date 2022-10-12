package com.nttdata.bootcamp.mscustomers.interfaces;

import com.nttdata.bootcamp.mscustomers.model.Customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerService {
    public Mono<Customer> createCustomer(Mono<Customer> customer);

    public Flux<Customer> findAllCustomers();
}

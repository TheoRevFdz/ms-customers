package com.nttdata.bootcamp.mscustomers.interfaces;

import java.time.Duration;

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
        return repository.findAll().delayElements(Duration.ofSeconds(1)).log();
    }

    @Override
    public Mono<Customer> updateCustomer(Customer customer) {
        return repository.findById(customer.getId())
                .map(c -> customer)
                .flatMap(repository::save);
    }

    @Override
    public boolean deleteCustomer(String id) {
        Mono<Customer> cusFinded = repository.findById(id);
        if (cusFinded != null) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

}

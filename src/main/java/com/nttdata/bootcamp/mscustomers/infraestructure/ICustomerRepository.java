package com.nttdata.bootcamp.mscustomers.infraestructure;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.bootcamp.mscustomers.model.Customer;

@Repository("ICustomerRepository")
public interface ICustomerRepository extends MongoRepository<Customer, String> {
    public Customer findByNroDoc(String nroDoc);
}

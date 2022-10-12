package com.nttdata.bootcamp.mscustomers.application;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.bootcamp.mscustomers.interfaces.ICustomerService;
import com.nttdata.bootcamp.mscustomers.model.Customer;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
public class CustomerController {
    @Autowired
    ICustomerService service;

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody Mono<Customer> customer) {
        try {
            final Mono<Customer> response = service.createCustomer(customer);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error al crear cliente"));
        }
    }
}

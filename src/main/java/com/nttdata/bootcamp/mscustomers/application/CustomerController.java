package com.nttdata.bootcamp.mscustomers.application;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.bootcamp.mscustomers.enums.CustomerTypes;
import com.nttdata.bootcamp.mscustomers.interfaces.ICustomerService;
import com.nttdata.bootcamp.mscustomers.model.Customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
public class CustomerController {
    @Autowired
    ICustomerService service;

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        try {
            if (customer != null && (customer.getType().equals(CustomerTypes.PERSONAL.toString())
                    || customer.getType().equals(CustomerTypes.EMPRESARIAL.toString()))) {
                final Mono<Customer> customerMono = Mono.just(customer);
                final Mono<Customer> response = service.createCustomer(customerMono);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap(
                            "message",
                            "El cliente solo puede ser de tipo " +
                                    CustomerTypes.PERSONAL.toString() +
                                    " o de tipo " +
                                    CustomerTypes.EMPRESARIAL.toString() + "."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error al crear cliente."));
        }
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<?> findAllCustomers() {
        try {
            final Flux<Customer> response = service.findAllCustomers();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error en servidor al obtener los datos de clientes."));
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        try {
            if (customer != null && (customer.getType().equals(CustomerTypes.PERSONAL.toString())
                    || customer.getType().equals(CustomerTypes.EMPRESARIAL.toString()))) {
                Mono<Customer> response = service.updateCustomer(customer);
                if (response != null) {
                    return ResponseEntity.ok(response);
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap(
                                "message",
                                "El cliente que intenta actualizar no existe."));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap(
                            "message",
                            "El cliente solo puede ser de tipo " +
                                    CustomerTypes.PERSONAL.toString() +
                                    " o de tipo " +
                                    CustomerTypes.EMPRESARIAL.toString() + "."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error en servidor al actualizar cliente."));
        }
    }
}

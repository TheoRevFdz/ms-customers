package com.nttdata.bootcamp.mscustomers.application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.bootcamp.mscustomers.enums.CustomerTypes;
import com.nttdata.bootcamp.mscustomers.interfaces.ICustomerService;
import com.nttdata.bootcamp.mscustomers.model.Customer;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RefreshScope
@RestController
public class CustomerController {
    @Autowired
    private ICustomerService service;

    @Value("message.demo")
    private String demoString;

    private final String endPoint = "customers";

    @PostMapping(endPoint)
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        try {
            if (customer != null && customer.getTypePerson() != null
                    && (customer.getTypePerson().equals(CustomerTypes.PERSONAL.toString())
                            || customer.getTypePerson().equals(CustomerTypes.EMPRESARIAL.toString()))) {
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

    @GetMapping(value = endPoint, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<?> findAllCustomers() {
        try {
            log.info(demoString);
            final Flux<Customer> response = service.findAllCustomers();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error en servidor al obtener los datos de clientes."));
        }
    }

    @PutMapping(endPoint)
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        try {
            if (customer != null && (customer.getTypePerson().equals(CustomerTypes.PERSONAL.toString())
                    || customer.getTypePerson().equals(CustomerTypes.EMPRESARIAL.toString()))) {
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

    @GetMapping(endPoint + "/byNroDoc/{nroDoc}")
    public ResponseEntity<?> findCustomerByNroDoc(@PathVariable String nroDoc) {
        try {
            final Customer response = service.findCustomerByNroDoc(nroDoc);
            if (response != null) {
                Map<String,Object> res=new HashMap<>();
                res.put("id", response.getId());
                res.put("firstName",response.getFirstName());
                res.put("lastName",response.getLastName());
                res.put("typeDoc",response.getTypeDoc());
                res.put("nroDoc",response.getNroDoc());
                res.put("phone",response.getPhone());
                res.put("email",response.getEmail());
                res.put("typePerson",response.getTypePerson());
                res.put("typeProduct",response.getTypeProduct());
                res.put("regDate",response.getRegDate());
                return ResponseEntity.ok(res);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

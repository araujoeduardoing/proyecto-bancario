package liq_msa_bp_customer.infrastructure.input.adapter.rest.impl;

import liq_msa_bp_customer.application.input.port.CustomerService;
import liq_msa_bp_customer.domain.Customer;
import liq_msa_bp_customer.infrastructure.exception.CustomerNotFoundException;
import liq_msa_bp_customer.infrastructure.input.adapter.rest.bean.CustomerRequest;
import liq_msa_bp_customer.infrastructure.repository.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/business/retail/v1/customers")
@Tag(name = "Customer", description = "Customer management API")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerMapper customerMapper;


    @PostMapping("/register")
    @Operation(summary = "Create a new customer")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerRequest request) {
        Customer customer = customerMapper.customerRequestToCustomerDomain(request);
        Customer savedCustomer = customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    @GetMapping("/{id:[0-9]+}")
    @Operation(summary = "Get customer by ID")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Cliente no encontrado con ID: " + id);
        }
        return ResponseEntity.ok(customer.get());
    }
    @PutMapping("/{id:[0-9]+}")
    @Operation(summary = "Update customer")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id,
                                                   @Valid @RequestBody CustomerRequest request) {
        // Primero verificar que el cliente existe
        Optional<Customer> existingCustomerOpt = customerService.findById(id);
        if (existingCustomerOpt.isEmpty()) {
            throw new CustomerNotFoundException("Cliente no encontrado con ID: " + id);
        }
        
        Customer existingCustomer = existingCustomerOpt.get();
        
        // Actualizar los campos del cliente existente con los datos del request
        existingCustomer.setName(request.getName());
        existingCustomer.setGender(request.getGender());
        existingCustomer.setAge(request.getAge());
        existingCustomer.setIdentification(request.getIdentification());
        existingCustomer.setAddress(request.getAddress());
        existingCustomer.setPhone(request.getPhone());
        existingCustomer.setPassword(request.getPassword());
        existingCustomer.setStatus(request.getStatus());
        
        Customer updatedCustomer = customerService.save(existingCustomer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id:[0-9]+}")
    @Operation(summary = "Delete customer")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
   
    @GetMapping("/all")
    @Operation(summary = "Get all customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.findAll();
        return customers.isEmpty()
                ? ResponseEntity.ok(List.of())
                : ResponseEntity.ok(customers);
    }

}

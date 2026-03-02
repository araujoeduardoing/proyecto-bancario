package liq_msa_bp_customer.infrastructure.repository;

import liq_msa_bp_customer.domain.Customer;

import java.util.List;
import java.util.Optional;


public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(Long id);
    void deleteById(Long id);
    List<Customer> findAll();

}

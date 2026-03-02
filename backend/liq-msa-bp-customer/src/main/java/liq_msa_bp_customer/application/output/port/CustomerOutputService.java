package liq_msa_bp_customer.application.output.port;

import liq_msa_bp_customer.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerOutputService {
    Customer save(Customer customer);
    Optional<Customer> findById(Long id);
    void deleteById(Long id);
    List<Customer> findAll();

}

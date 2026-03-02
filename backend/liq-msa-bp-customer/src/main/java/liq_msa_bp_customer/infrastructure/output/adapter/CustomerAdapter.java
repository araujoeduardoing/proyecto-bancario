package liq_msa_bp_customer.infrastructure.output.adapter;

import liq_msa_bp_customer.application.output.port.CustomerOutputService;
import liq_msa_bp_customer.domain.Customer;
import liq_msa_bp_customer.infrastructure.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerAdapter implements CustomerOutputService {
    @Autowired
    private CustomerRepository customerRepository;
    public Customer save(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}

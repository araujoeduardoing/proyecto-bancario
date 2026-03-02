package liq_msa_bp_customer.application.service;

import liq_msa_bp_customer.application.input.port.CustomerService;
import liq_msa_bp_customer.application.output.port.CustomerOutputService;
import liq_msa_bp_customer.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerOutputService customerOutputService;
    @Override
    public Customer save(Customer customer) {
        return customerOutputService.save(customer);
    }
    @Override
    public Optional<Customer> findById(Long id) {
        return customerOutputService.findById(id);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        if (!customerOutputService.findById(id).isPresent()) {
            throw new IllegalArgumentException("Cliente no encontrado con ID: " + id);
        }
        customerOutputService.deleteById(id);
    }

    @Override
    public List<Customer> findAll() {
        return customerOutputService.findAll();
    }
}

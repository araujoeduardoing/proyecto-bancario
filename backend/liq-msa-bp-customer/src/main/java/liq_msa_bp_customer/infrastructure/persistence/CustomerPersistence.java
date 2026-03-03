package liq_msa_bp_customer.infrastructure.persistence;

import liq_msa_bp_customer.domain.Customer;
import liq_msa_bp_customer.infrastructure.persistence.jpa.CustomerJpaRepository;
import liq_msa_bp_customer.infrastructure.repository.CustomerRepository;
import liq_msa_bp_customer.infrastructure.repository.entity.CustomerEntity;
import liq_msa_bp_customer.infrastructure.repository.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomerPersistence implements CustomerRepository {
    private final CustomerJpaRepository jpaRepository;
    private final CustomerMapper customerMapper;
    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = customerMapper.customertoCustomerEntity(customer);
        CustomerEntity savedEntity = jpaRepository.save(entity);
        return customerMapper.customerEntityToCustomerDomain(savedEntity);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return jpaRepository.findByClientId(id)
                .map(customerMapper::customerEntityToCustomerDomain);
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteByClientId(id);
    }

    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(customerMapper::customerEntityToCustomerDomain)
                .collect(Collectors.toList());
    }


}

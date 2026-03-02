package liq_msa_bp_customer.infrastructure.repository.mapper;

import liq_msa_bp_customer.domain.Customer;
import liq_msa_bp_customer.infrastructure.input.adapter.rest.bean.CustomerRequest;
import liq_msa_bp_customer.infrastructure.repository.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(CustomerMapper.class);

    CustomerEntity customertoCustomerEntity(Customer customer);
    Customer customerEntityToCustomerDomain(CustomerEntity entity);

    Customer customerRequestToCustomerDomain(CustomerRequest request);


    /*public static Customer toDomain(CustomerEntity entity) {
        if (entity == null) {
            return null;
        }

        Customer customer = new Customer();
        customer.setCustomerId(entity.getCustomerId());
        customer.setIdentification(entity.getIdentification());
        customer.setTypeIdentification(entity.getTypeIdentification());
        customer.setFirstName(entity.getFirstName());
        customer.setLastName(entity.getLastName());
        customer.setPhoneNumber(entity.getPhoneNumber());
        customer.setEmail(entity.getEmail());
        customer.setStatus(entity.getStatus());
        customer.setIdCompany(entity.getIdCompany());
        customer.setAddress(entity.getAddress());
        customer.setCreatedAt(entity.getCreatedAt());

        return customer;
    }

    public static CustomerEntity toEntity(Customer customer) {
        if (customer == null) {
            return null;
        }

        CustomerEntity entity = new CustomerEntity();
        entity.setCustomerId(customer.getCustomerId());
        entity.setIdentification(customer.getIdentification());
        entity.setTypeIdentification(customer.getTypeIdentification());
        entity.setFirstName(customer.getFirstName());
        entity.setLastName(customer.getLastName());
        entity.setPhoneNumber(customer.getPhoneNumber());
        entity.setEmail(customer.getEmail());
        entity.setStatus(customer.getStatus());
        entity.setIdCompany(customer.getIdCompany());
        entity.setAddress(customer.getAddress());
        entity.setCreatedAt(customer.getCreatedAt());

        return entity;
    }*/
}

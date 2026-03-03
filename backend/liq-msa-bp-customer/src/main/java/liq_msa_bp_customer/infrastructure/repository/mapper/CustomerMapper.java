package liq_msa_bp_customer.infrastructure.repository.mapper;

import liq_msa_bp_customer.domain.Customer;
import liq_msa_bp_customer.infrastructure.input.adapter.rest.bean.CustomerRequest;
import liq_msa_bp_customer.infrastructure.repository.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    // MapStruct maneja automáticamente los campos con nombres iguales
    CustomerEntity customertoCustomerEntity(Customer customer);
    
    // MapStruct maneja automáticamente los campos con nombres iguales  
    Customer customerEntityToCustomerDomain(CustomerEntity entity);

    // Mapeo de CustomerRequest a Customer - campos auto-generados se ignoran
    @Mapping(target = "personId", ignore = true) // Se genera automáticamente en la BD
    @Mapping(target = "clientId", ignore = true) // Se genera automáticamente en la BD
    Customer customerRequestToCustomerDomain(CustomerRequest request);

}

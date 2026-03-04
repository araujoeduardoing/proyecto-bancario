package liq_msa_bp_account.infrastructure.repository.mapper;

import liq_msa_bp_account.domain.Account;
import liq_msa_bp_account.infrastructure.input.adapter.rest.bean.AccountRequest;
import liq_msa_bp_account.infrastructure.repository.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    // MapStruct maneja automáticamente los campos con nombres iguales
    AccountEntity accountToAccountEntity(Account account);
    
    // MapStruct maneja automáticamente los campos con nombres iguales  
    Account accountEntityToAccountDomain(AccountEntity entity);

    // Mapeo de AccountRequest a Account - campos auto-generados se ignoran
    @Mapping(target = "id", ignore = true) // Se genera automáticamente en la BD
    @Mapping(target = "fechaCreacion", ignore = true) 
    @Mapping(target = "fechaActualizacion", ignore = true)
    Account accountRequestToAccountDomain(AccountRequest request);

}

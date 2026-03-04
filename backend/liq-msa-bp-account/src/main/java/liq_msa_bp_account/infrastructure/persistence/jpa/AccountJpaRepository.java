package liq_msa_bp_account.infrastructure.persistence.jpa;

import liq_msa_bp_account.infrastructure.repository.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
    
    // Buscar por numero de cuenta
    Optional<AccountEntity> findByAccountNumber(String accountNumber);
    
    // Verificar si existe por numero de cuenta
    boolean existsByAccountNumber(String accountNumber);
    
}

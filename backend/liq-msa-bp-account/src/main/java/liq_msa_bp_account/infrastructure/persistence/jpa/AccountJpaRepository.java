package liq_msa_bp_account.infrastructure.persistence.jpa;

import liq_msa_bp_account.infrastructure.repository.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
    
    // Buscar por numero de cuenta
    Optional<AccountEntity> findByAccountNumber(String accountNumber);
    
    // Verificar si existe por numero de cuenta
    boolean existsByAccountNumber(String accountNumber);
    
    // Consulta JPQL para obtener todas las cuentas con el nombre de la persona
    @Query("SELECT a.id, a.accountNumber, a.accountType, a.initialBalance, a.status, a.clientId, a.customer.person.name, a.createdAt, a.updatedAt " +
           "FROM AccountEntity a " +
           "JOIN a.customer c " +
           "JOIN c.person p")
    List<Object[]> findAllWithPersonName();
    
    // Buscar cuentas por ID de cliente
    List<AccountEntity> findByClientId(Long clientId);
    
}

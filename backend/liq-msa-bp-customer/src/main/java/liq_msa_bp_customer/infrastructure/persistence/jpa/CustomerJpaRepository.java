package liq_msa_bp_customer.infrastructure.persistence.jpa;

import liq_msa_bp_customer.infrastructure.repository.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
    
    // Buscar por client_id en lugar de person_id
    @Query("SELECT c FROM CustomerEntity c WHERE c.clientId = :clientId")
    Optional<CustomerEntity> findByClientId(@Param("clientId") Long clientId);
    
    // Eliminar por client_id
    @Query("DELETE FROM CustomerEntity c WHERE c.clientId = :clientId")
    @Modifying
    void deleteByClientId(@Param("clientId") Long clientId);
    
    // Verificar si existe por client_id
    boolean existsByClientId(Long clientId);
    
}

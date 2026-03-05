package liq_msa_bp_movements.infrastructure.persistence.jpa;

import liq_msa_bp_movements.infrastructure.repository.entity.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementJpaRepository extends JpaRepository<MovementEntity, Long> {
    
    // Buscar movimientos por client_id
    @Query("SELECT m FROM MovementEntity m WHERE m.clientId = :clientId ORDER BY m.movementDate DESC")
    List<MovementEntity> findByClientId(@Param("clientId") Long clientId);
    
    // Buscar movimientos por account_id
    @Query("SELECT m FROM MovementEntity m WHERE m.accountId = :accountId ORDER BY m.movementDate DESC")
    List<MovementEntity> findByAccountId(@Param("accountId") Long accountId);
    
    // Buscar movimientos por client_id y account_id
    @Query("SELECT m FROM MovementEntity m WHERE m.clientId = :clientId AND m.accountId = :accountId ORDER BY m.movementDate DESC")
    List<MovementEntity> findByClientIdAndAccountId(@Param("clientId") Long clientId, @Param("accountId") Long accountId);
    
    // Verificar si existe un movimiento
    boolean existsByMovementId(Long movementId);
}
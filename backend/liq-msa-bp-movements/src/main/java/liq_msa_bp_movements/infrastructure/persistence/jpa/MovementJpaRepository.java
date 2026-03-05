package liq_msa_bp_movements.infrastructure.persistence.jpa;

import liq_msa_bp_movements.infrastructure.repository.entity.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementJpaRepository extends JpaRepository<MovementEntity, Long> {
    
    @Query("SELECT m FROM MovementEntity m WHERE m.clientId = :clientId ORDER BY m.movementDate DESC")
    List<MovementEntity> findByClientId(@Param("clientId") Long clientId);
    
    @Query("SELECT m FROM MovementEntity m WHERE m.accountId = :accountId ORDER BY m.movementDate DESC")
    List<MovementEntity> findByAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT m FROM MovementEntity m WHERE m.clientId = :clientId AND m.accountId = :accountId ORDER BY m.movementDate DESC")
    List<MovementEntity> findByClientIdAndAccountId(@Param("clientId") Long clientId, @Param("accountId") Long accountId);
    
    boolean existsByMovementId(Long movementId);
    
    @Query("""
        SELECT 
            m.movementId,
            m.movementDate,
            m.clientId,
            m.accountId,
            m.movementType,
            m.initialBalance,
            m.movementStatus,
            m.amount,
            m.availableBalance,
            m.createdAt,
            m.updatedAt,
            a.accountNumber,
            p.name
        FROM MovementEntity m
        JOIN m.account a
        JOIN m.customer c
        JOIN c.person p
        ORDER BY m.movementDate DESC
        """)
    List<Object[]> findAllMovementsWithDetails();
    
    @Query("""
        SELECT 
            m.movementId,
            m.movementDate,
            m.clientId,
            m.accountId,
            m.movementType,
            m.initialBalance,
            m.movementStatus,
            m.amount,
            m.availableBalance,
            m.createdAt,
            m.updatedAt,
            a.accountNumber,
            p.name
        FROM MovementEntity m
        JOIN m.account a
        JOIN m.customer c
        JOIN c.person p
        WHERE m.clientId = :clientId
        ORDER BY m.movementDate DESC
        """)    
    List<Object[]> findMovementsWithDetailsByClientId(@Param("clientId") Long clientId);
}
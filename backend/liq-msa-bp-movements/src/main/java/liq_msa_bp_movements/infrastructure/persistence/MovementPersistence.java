package liq_msa_bp_movements.infrastructure.persistence;

import liq_msa_bp_movements.domain.Movement;
import liq_msa_bp_movements.domain.MovementWithDetails;
import liq_msa_bp_movements.infrastructure.persistence.jpa.MovementJpaRepository;
import liq_msa_bp_movements.infrastructure.repository.MovementRepository;
import liq_msa_bp_movements.infrastructure.repository.entity.MovementEntity;
import liq_msa_bp_movements.infrastructure.repository.mapper.MovementMapper;
import liq_msa_bp_movements.infrastructure.repository.mapper.MovementDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MovementPersistence implements MovementRepository {
    private final MovementJpaRepository jpaRepository;
    private final MovementMapper movementMapper;
    private final MovementDetailsMapper movementDetailsMapper;

    @Override
    public Movement save(Movement movement) {
        MovementEntity entity = movementMapper.movementToMovementEntity(movement);
        MovementEntity savedEntity = jpaRepository.save(entity);
        return movementMapper.movementEntityToMovementDomain(savedEntity);
    }

    @Override
    public Optional<Movement> findById(Long id) {
        return jpaRepository.findById(id)
                .map(movementMapper::movementEntityToMovementDomain);
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Movement> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(movementMapper::movementEntityToMovementDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movement> findByClientId(Long clientId) {
        return jpaRepository.findByClientId(clientId)
                .stream()
                .map(movementMapper::movementEntityToMovementDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovementWithDetails> findAllWithDetails() {
        List<Object[]> results = jpaRepository.findAllMovementsWithDetails();
        return movementDetailsMapper.mapToMovementWithDetailsList(results);
    }

    @Override
    public List<MovementWithDetails> findByClientIdWithDetails(Long clientId) {
        List<Object[]> results = jpaRepository.findMovementsWithDetailsByClientId(clientId);
        return movementDetailsMapper.mapToMovementWithDetailsList(results);
    }

    @Override
    public List<MovementWithDetails> findAccountStatementByClientIdAndDateRange(Long clientId, LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = jpaRepository.findAccountStatementByClientIdAndDateRange(clientId, startDate, endDate);
        return movementDetailsMapper.mapToMovementWithDetailsList(results);
    }
}
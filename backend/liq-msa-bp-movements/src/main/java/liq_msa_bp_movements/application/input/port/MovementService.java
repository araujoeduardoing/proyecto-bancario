package liq_msa_bp_movements.application.input.port;

import liq_msa_bp_movements.domain.Movement;
import liq_msa_bp_movements.domain.MovementWithDetails;

import java.util.List;
import java.util.Optional;

public interface MovementService {
    Movement save(Movement movement);
    Optional<Movement> findById(Long id);
    void deleteById(Long id);
    List<Movement> findAll();
    List<Movement> findByClientId(Long clientId);
    
    List<MovementWithDetails> findAllWithDetails();
    List<MovementWithDetails> findByClientIdWithDetails(Long clientId);
}
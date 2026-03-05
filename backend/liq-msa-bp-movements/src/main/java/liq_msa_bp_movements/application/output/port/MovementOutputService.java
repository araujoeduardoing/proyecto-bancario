package liq_msa_bp_movements.application.output.port;

import liq_msa_bp_movements.domain.Movement;

import java.util.List;
import java.util.Optional;

public interface MovementOutputService {
    Movement save(Movement movement);
    Optional<Movement> findById(Long id);
    void deleteById(Long id);
    List<Movement> findAll();
    List<Movement> findByClientId(Long clientId);
}
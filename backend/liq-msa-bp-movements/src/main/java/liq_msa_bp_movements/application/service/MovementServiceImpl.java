package liq_msa_bp_movements.application.service;

import liq_msa_bp_movements.application.input.port.MovementService;
import liq_msa_bp_movements.application.output.port.MovementOutputService;
import liq_msa_bp_movements.domain.Movement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MovementServiceImpl implements MovementService {
    
    @Autowired
    private MovementOutputService movementOutputService;
    
    @Override
    public Movement save(Movement movement) {
        if (movement.getMovementId() == null) {
            // Nuevo movimiento
            movement.setMovementDate(LocalDateTime.now());
            movement.setCreatedAt(LocalDateTime.now());
        } else {
            // Actualización
            movement.setUpdatedAt(LocalDateTime.now());
        }
        return movementOutputService.save(movement);
    }

    @Override
    public Optional<Movement> findById(Long id) {
        return movementOutputService.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        if (!movementOutputService.findById(id).isPresent()) {
            throw new IllegalArgumentException("Movimiento no encontrado con ID: " + id);
        }
        movementOutputService.deleteById(id);
    }

    @Override
    public List<Movement> findAll() {
        return movementOutputService.findAll();
    }

    @Override
    public List<Movement> findByClientId(Long clientId) {
        return movementOutputService.findByClientId(clientId);
    }
}
package liq_msa_bp_movements.infrastructure.output.adapter;

import liq_msa_bp_movements.application.output.port.MovementOutputService;
import liq_msa_bp_movements.domain.Movement;
import liq_msa_bp_movements.domain.MovementWithDetails;
import liq_msa_bp_movements.infrastructure.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovementAdapter implements MovementOutputService {
    @Autowired
    private MovementRepository movementRepository;

    @Override
    public Movement save(Movement movement) {
        return movementRepository.save(movement);
    }

    @Override
    public Optional<Movement> findById(Long id) {
        return movementRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        movementRepository.deleteById(id);
    }

    @Override
    public List<Movement> findAll() {
        return movementRepository.findAll();
    }

    @Override
    public List<Movement> findByClientId(Long clientId) {
        return movementRepository.findByClientId(clientId);
    }

    @Override
    public List<MovementWithDetails> findAllWithDetails() {
        return movementRepository.findAllWithDetails();
    }

    @Override
    public List<MovementWithDetails> findByClientIdWithDetails(Long clientId) {
        return movementRepository.findByClientIdWithDetails(clientId);
    }
}
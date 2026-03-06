package liq_msa_bp_movements.infrastructure.output.adapter;

import liq_msa_bp_movements.application.output.port.MovementOutputService;
import liq_msa_bp_movements.domain.Movement;
import liq_msa_bp_movements.domain.MovementWithDetails;
import liq_msa_bp_movements.infrastructure.repository.MovementRepository;
import liq_msa_bp_movements.infrastructure.output.adapter.dto.AccountUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MovementAdapter implements MovementOutputService {
    @Autowired
    private MovementRepository movementRepository;
    
    @Autowired
    private WebClient webClient;
    
    @Value("${accounts.service.url:http://localhost:4102}")
    private String accountServiceUrl;

    @Override
    public Movement save(Movement movement) {
        // Guardar el movimiento primero
        Movement savedMovement = movementRepository.save(movement);
        
        // Actualizar el initial balance en el microservicio de cuentas
        if (movement.getAccountId() != null && movement.getAvailableBalance() != null) {
            updateAccountInitialBalance(movement.getAccountId(), movement.getAvailableBalance());
        }
        
        return savedMovement;
    }
    
    private void updateAccountInitialBalance(Long accountId, BigDecimal availableBalance) {
        try {
            AccountUpdateRequest request = new AccountUpdateRequest(availableBalance);
            
            webClient.patch()
                .uri(accountServiceUrl + "/business/retail/v1/accounts/{accountId}", accountId)
                .header("Accept", "*/*")
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("Account {} initial balance updated successfully", accountId))
                .doOnError(throwable -> log.error("Error updating initial balance for account {}: {}", accountId, throwable.getMessage()))
                .onErrorResume(throwable -> {
                    log.warn("Failed to update account balance, continuing with movement save");
                    return Mono.empty();
                })
                .subscribe();
                
        } catch (Exception e) {
            log.error("Exception updating initial balance for account {}: {}", accountId, e.getMessage());
        }
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

    @Override
    public List<MovementWithDetails> findAccountStatementByClientIdAndDateRange(Long clientId, LocalDate startDate, LocalDate endDate) {
        return movementRepository.findAccountStatementByClientIdAndDateRange(clientId, startDate, endDate);
    }
}
package liq_msa_bp_movements.infrastructure.input.adapter.rest.impl;

import liq_msa_bp_movements.application.input.port.MovementService;
import liq_msa_bp_movements.domain.Movement;
import liq_msa_bp_movements.infrastructure.exception.MovementNotFoundException;
import liq_msa_bp_movements.infrastructure.input.adapter.rest.bean.MovementRequest;
import liq_msa_bp_movements.infrastructure.repository.mapper.MovementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/business/retail/v1/movements")
@Tag(name = "Movement", description = "Movement management API")
public class MovementController {
    
    @Autowired
    private MovementService movementService;
    
    @Autowired
    private MovementMapper movementMapper;

    @PostMapping("/register")
    @Operation(summary = "Create a new movement")
    public ResponseEntity<Movement> createMovement(@Valid @RequestBody MovementRequest request) {
        Movement movement = movementMapper.movementRequestToMovementDomain(request);
        Movement savedMovement = movementService.save(movement);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovement);
    }

    @GetMapping("/{id:[0-9]+}")
    @Operation(summary = "Get movement by ID")
    public ResponseEntity<Movement> getMovementById(@PathVariable Long id) {
        Optional<Movement> movement = movementService.findById(id);
        if (movement.isEmpty()) {
            throw new MovementNotFoundException("Movimiento no encontrado con ID: " + id);
        }
        return ResponseEntity.ok(movement.get());
    }

    @PutMapping("/{id:[0-9]+}")
    @Operation(summary = "Update movement")
    public ResponseEntity<Movement> updateMovement(@PathVariable Long id,
                                                   @Valid @RequestBody MovementRequest request) {
        Optional<Movement> existingMovementOpt = movementService.findById(id);
        if (existingMovementOpt.isEmpty()) {
            throw new MovementNotFoundException("Movimiento no encontrado con ID: " + id);
        }
        
        Movement existingMovement = existingMovementOpt.get();
        
        // Actualizar los campos del movimiento existente
        existingMovement.setClientId(request.getClientId());
        existingMovement.setAccountId(request.getAccountId());
        existingMovement.setMovementType(request.getMovementType());
        existingMovement.setInitialBalance(request.getInitialBalance());
        existingMovement.setMovementStatus(request.getMovementStatus());
        existingMovement.setAmount(request.getAmount());
        existingMovement.setAvailableBalance(request.getAvailableBalance());
        
        Movement updatedMovement = movementService.save(existingMovement);
        return ResponseEntity.ok(updatedMovement);
    }

    @DeleteMapping("/{id:[0-9]+}")
    @Operation(summary = "Cancel movement")
    public ResponseEntity<Void> cancelMovement(@PathVariable Long id) {
        movementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
   
    @GetMapping("/all")
    @Operation(summary = "Get all movements")
    public ResponseEntity<List<Movement>> getAllMovements() {
        List<Movement> movements = movementService.findAll();
        return movements.isEmpty()
                ? ResponseEntity.ok(List.of())
                : ResponseEntity.ok(movements);
    }

    @GetMapping("/client/{clientId:[0-9]+}")
    @Operation(summary = "Get movements by client ID")
    public ResponseEntity<List<Movement>> getMovementsByClientId(@PathVariable Long clientId) {
        List<Movement> movements = movementService.findByClientId(clientId);
        return movements.isEmpty()
                ? ResponseEntity.ok(List.of())
                : ResponseEntity.ok(movements);
    }
}
package liq_msa_bp_movements.infrastructure.input.adapter.rest.bean;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import liq_msa_bp_movements.domain.MovementType;
import liq_msa_bp_movements.domain.MovementStatus;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class MovementRequest {
    
    @NotNull(message = "El ID del cliente es requerido")
    private Long clientId;
    
    @NotNull(message = "El ID de la cuenta es requerido")
    private Long accountId;
    
    @NotNull(message = "El tipo de movimiento es requerido")
    private MovementType movementType;
    
    @NotNull(message = "El saldo inicial es requerido")
    @DecimalMin(value = "0.0", message = "El saldo inicial debe ser mayor o igual a 0")
    private BigDecimal initialBalance;
    
    private MovementStatus movementStatus = MovementStatus.ACTIVE;
    
    @NotNull(message = "El monto es requerido")
    private BigDecimal amount;
    
    @NotNull(message = "El saldo disponible es requerido")
    @DecimalMin(value = "0.0", message = "El saldo disponible debe ser mayor o igual a 0")
    private BigDecimal availableBalance;
}
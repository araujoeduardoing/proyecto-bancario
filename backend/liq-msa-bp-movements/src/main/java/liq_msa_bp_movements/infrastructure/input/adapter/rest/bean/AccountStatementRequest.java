package liq_msa_bp_movements.infrastructure.input.adapter.rest.bean;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class AccountStatementRequest {
    
    @NotNull(message = "El ID del cliente es requerido")
    private Long clientId;
    
    @NotNull(message = "La fecha de inicio es requerida")
    private LocalDate startDate;
    
    @NotNull(message = "La fecha de fin es requerida")
    private LocalDate endDate;
}
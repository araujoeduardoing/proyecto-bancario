package liq_msa_bp_movements.domain;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Movement {
    private Long movementId;
    private LocalDateTime movementDate;
    private Long clientId;
    private Long accountId;
    private MovementType movementType;
    private BigDecimal initialBalance;
    private MovementStatus movementStatus;
    private BigDecimal amount;
    private BigDecimal availableBalance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
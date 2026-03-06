package liq_msa_bp_movements.infrastructure.output.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateRequest {
    private BigDecimal initialBalance;
}
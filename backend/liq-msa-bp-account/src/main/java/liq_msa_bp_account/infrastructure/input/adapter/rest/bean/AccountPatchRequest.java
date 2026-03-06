package liq_msa_bp_account.infrastructure.input.adapter.rest.bean;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountPatchRequest {
    


    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Initial balance cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Initial balance must not exceed 13 integer digits and 2 decimal places")
    private BigDecimal initialBalance;

}

package liq_msa_bp_account.infrastructure.input.adapter.rest.bean;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequest {
    
    @NotBlank(message = "Account number is required")
    @Size(max = 20, message = "Account number must not exceed 20 characters")
    private String accountNumber;
    
    @NotBlank(message = "Account type is required")
    @Size(max = 50, message = "Account type must not exceed 50 characters")
    private String accountType;
    
    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Initial balance cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Initial balance must not exceed 13 integer digits and 2 decimal places")
    private BigDecimal initialBalance;
    
    @NotNull(message = "Status is required")
    private Boolean status;
    
    @NotNull(message = "Client ID is required")
    private Long clientId;
}

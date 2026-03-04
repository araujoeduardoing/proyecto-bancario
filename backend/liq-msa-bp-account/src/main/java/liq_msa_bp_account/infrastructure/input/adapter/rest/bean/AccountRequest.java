package liq_msa_bp_account.infrastructure.input.adapter.rest.bean;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequest {
    
    @NotBlank(message = "El número de cuenta es requerido")
    @Size(max = 20, message = "El número de cuenta no debe exceder 20 caracteres")
    private String numeroCuenta;
    
    @NotBlank(message = "El tipo de cuenta es requerido")
    @Size(max = 50, message = "El tipo de cuenta no debe exceder 50 caracteres")
    private String tipoCuenta;
    
    @NotNull(message = "El saldo inicial es requerido")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial no puede ser negativo")
    @Digits(integer = 13, fraction = 2, message = "El saldo inicial no debe exceder 13 dígitos enteros y 2 decimales")
    private BigDecimal saldoInicial;
    
    @NotNull(message = "El estado es requerido")
    private Boolean estado;
}

package liq_msa_bp_customer.infrastructure.input.adapter.rest.bean;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import liq_msa_bp_customer.domain.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {
    
    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;
    
    private Boolean status;
    
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 250, message = "El nombre no debe exceder 250 caracteres")
    private String name;
    
    @Size(max = 100, message = "El género no debe exceder 100 caracteres")
    private String gender;
    
    private Integer age;
    
    @NotBlank(message = "La identificación es requerida")
    @Size(max = 100, message = "La identificación no debe exceder 100 caracteres")
    private String identification;
    
    @Size(max = 100, message = "La dirección no debe exceder 100 caracteres")
    private String address;
    
    @Size(max = 100, message = "El teléfono no debe exceder 100 caracteres")
    private String phone;
}

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
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
    
    private Boolean status;
    
    @NotBlank(message = "Name is required")
    @Size(max = 250, message = "Name must not exceed 250 characters")
    private String name;
    
    @Size(max = 100, message = "Gender must not exceed 100 characters")
    private String gender;
    
    private Integer age;
    
    @NotBlank(message = "Identification is required")
    @Size(max = 100, message = "Identification must not exceed 100 characters")
    private String identification;
    
    @Size(max = 100, message = "Address must not exceed 100 characters")
    private String address;
    
    @Size(max = 100, message = "Phone must not exceed 100 characters")
    private String phone;
}

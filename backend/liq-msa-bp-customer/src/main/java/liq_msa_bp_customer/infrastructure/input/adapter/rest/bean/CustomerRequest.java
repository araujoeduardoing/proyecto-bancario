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
    
    @NotBlank(message = "Identification is required")
    @Size(max = 20, message = "Identification must not exceed 20 characters")
    private String identification;
    
    @NotBlank(message = "Type identification is required")
    @Size(max = 10, message = "Type identification must not exceed 10 characters")
    private String typeIdentification;
    
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;
    
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @Size(max = 20, message = "Company must not exceed 20 characters")
    private String idCompany;
    
    @Size(max = 250, message = "Address must not exceed 250 characters")
    private String address;
    
    private Boolean status;

    public CustomerRequest() {}


    /*
    public Customer toDomain() {
        Customer customer = new Customer();
        customer.setIdentification(this.identification);
        customer.setTypeIdentification(this.typeIdentification);
        customer.setFirstName(this.firstName);
        customer.setLastName(this.lastName);
        customer.setPhoneNumber(this.phoneNumber);
        customer.setEmail(this.email);
        customer.setIdCompany(this.idCompany);
        customer.setAddress(this.address);
        customer.setStatus(this.status != null ? this.status : true);
        return customer;
    }*/
}

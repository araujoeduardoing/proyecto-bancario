package liq_msa_bp_customer.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {
    private Long customerId;
    private String password;
    private String status;



}

package liq_msa_bp_customer.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer extends Person {
    private String clientId;
    private String password;
    private Boolean status;
}

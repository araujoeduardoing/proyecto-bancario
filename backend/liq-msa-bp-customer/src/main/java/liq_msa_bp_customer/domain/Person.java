package liq_msa_bp_customer.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {
    private Long personId;
    private String name;
    private String gender;
    private Integer age;
    private String identification;
    private String address;
    private String phone;
}
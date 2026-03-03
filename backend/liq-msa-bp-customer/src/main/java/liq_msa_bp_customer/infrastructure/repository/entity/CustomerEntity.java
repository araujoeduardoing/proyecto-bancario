package liq_msa_bp_customer.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@Table(name = "customer")
public class CustomerEntity extends PersonEntity {

    @Column(name = "client_id", unique = true, nullable = false, insertable = false, updatable = false)
    private Long clientId;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "status")
    private Boolean status = true;

    public CustomerEntity() {}


}

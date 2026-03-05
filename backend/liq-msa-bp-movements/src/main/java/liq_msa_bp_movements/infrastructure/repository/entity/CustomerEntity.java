package liq_msa_bp_movements.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "customer")
public class CustomerEntity {

    @Id
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "name")
    private String name;
}
package liq_msa_bp_account.infrastructure.repository.entity;

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

    @Column(name = "person_id")
    private Long personId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", insertable = false, updatable = false)
    private PersonEntity person;
}
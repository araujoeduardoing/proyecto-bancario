package liq_msa_bp_account.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter  
@Setter
@Table(name = "person")
public class PersonEntity {
    @Id
    @Column(name = "person_id")
    private Long personId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
}
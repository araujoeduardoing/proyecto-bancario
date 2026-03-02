package liq_msa_bp_customer.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long personId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "gender", length = 100)
    private String gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "identification", length = 100, unique = true)
    private String identification;

    @Column(name = "address", length = 100)
    private String address;
    
    @Column(name = "phone", length = 100)
    private String phone;
}

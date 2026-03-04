package liq_msa_bp_account.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_number", length = 20, unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "account_type", length = 50, nullable = false)
    private String accountType;

    @Column(name = "initial_balance", precision = 15, scale = 2, nullable = false)
    private BigDecimal initialBalance = BigDecimal.ZERO;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public AccountEntity() {}
}

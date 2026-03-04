package liq_msa_bp_account.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "cuenta")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_cuenta", length = 20, unique = true, nullable = false)
    private String numeroCuenta;

    @Column(name = "tipo_cuenta", length = 50, nullable = false)
    private String tipoCuenta;

    @Column(name = "saldo_inicial", precision = 15, scale = 2, nullable = false)
    private BigDecimal saldoInicial = BigDecimal.ZERO;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    public AccountEntity() {}
}

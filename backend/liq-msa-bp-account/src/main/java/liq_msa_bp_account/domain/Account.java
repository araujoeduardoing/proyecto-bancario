package liq_msa_bp_account.domain;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Account {
    private Long id;
    private String accountNumber;
    private String accountType;
    private BigDecimal initialBalance;
    private Boolean status;
    private Long clientId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

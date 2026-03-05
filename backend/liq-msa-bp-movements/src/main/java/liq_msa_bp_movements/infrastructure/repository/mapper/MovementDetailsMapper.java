package liq_msa_bp_movements.infrastructure.repository.mapper;

import liq_msa_bp_movements.domain.MovementWithDetails;
import liq_msa_bp_movements.domain.MovementType;
import liq_msa_bp_movements.domain.MovementStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MovementDetailsMapper {

    public MovementWithDetails mapToMovementWithDetails(Object[] row) {
        MovementWithDetails movement = new MovementWithDetails();
        
        movement.setMovementId(((Number) row[0]).longValue());
        movement.setMovementDate((LocalDateTime) row[1]);
        movement.setClientId(((Number) row[2]).longValue());
        movement.setAccountId(((Number) row[3]).longValue());
        movement.setMovementType((MovementType) row[4]);
        movement.setInitialBalance((BigDecimal) row[5]);
        movement.setMovementStatus((MovementStatus) row[6]);
        movement.setAmount((BigDecimal) row[7]);
        movement.setAvailableBalance((BigDecimal) row[8]);
        movement.setCreatedAt((LocalDateTime) row[9]);
        movement.setUpdatedAt((LocalDateTime) row[10]);
        movement.setAccountNumber((String) row[11]);
        movement.setClientName((String) row[12]);
        
        return movement;
    }

    public List<MovementWithDetails> mapToMovementWithDetailsList(List<Object[]> rows) {
        List<MovementWithDetails> movements = new ArrayList<>();
        for (Object[] row : rows) {
            movements.add(mapToMovementWithDetails(row));
        }
        return movements;
    }
}
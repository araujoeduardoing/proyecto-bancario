package liq_msa_bp_movements.infrastructure.repository.mapper;

import liq_msa_bp_movements.domain.Movement;
import liq_msa_bp_movements.infrastructure.input.adapter.rest.bean.MovementRequest;
import liq_msa_bp_movements.infrastructure.repository.entity.MovementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    MovementEntity movementToMovementEntity(Movement movement);
    
    Movement movementEntityToMovementDomain(MovementEntity entity);

    @Mapping(target = "movementId", ignore = true)
    @Mapping(target = "movementDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Movement movementRequestToMovementDomain(MovementRequest request);
}
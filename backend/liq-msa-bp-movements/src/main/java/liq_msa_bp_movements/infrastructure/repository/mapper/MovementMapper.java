package liq_msa_bp_movements.infrastructure.repository.mapper;

import liq_msa_bp_movements.domain.Movement;
import liq_msa_bp_movements.infrastructure.input.adapter.rest.bean.MovementRequest;
import liq_msa_bp_movements.infrastructure.repository.entity.MovementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    // MapStruct maneja automáticamente los campos con nombres iguales
    MovementEntity movementToMovementEntity(Movement movement);
    
    // MapStruct maneja automáticamente los campos con nombres iguales  
    Movement movementEntityToMovementDomain(MovementEntity entity);

    // Mapeo de MovementRequest a Movement - campos auto-generados se ignoran
    @Mapping(target = "movementId", ignore = true) // Se genera automáticamente en la BD
    @Mapping(target = "movementDate", ignore = true) // Se genera automáticamente
    @Mapping(target = "createdAt", ignore = true) // Se genera automáticamente
    @Mapping(target = "updatedAt", ignore = true) // Se genera automáticamente
    Movement movementRequestToMovementDomain(MovementRequest request);
}
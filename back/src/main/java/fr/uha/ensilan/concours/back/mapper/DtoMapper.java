package fr.uha.ensilan.concours.back.mapper;

import jakarta.annotation.Nullable;

public interface DtoMapper <MODEL, DTO>{
    DTO toDto(MODEL model);
    MODEL toModel(DTO dto);
    Iterable<DTO> toDto(Iterable<MODEL> models);
    Iterable<MODEL> toModel(Iterable<DTO> dtos);
    default @Nullable String getEntityGraphHint() {
        return null;
    }
}

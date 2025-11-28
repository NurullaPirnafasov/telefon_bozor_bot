package com.example.telefonbozor.mapper;

public interface Mapper<E, CD, UD, D> {
    E toEntity(CD dto);
    D toDto(E entity);
    void updateEntity(UD dto, E entity);
}

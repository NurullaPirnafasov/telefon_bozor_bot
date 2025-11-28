package com.example.telefonbozor.service.base;

import com.example.telefonbozor.mapper.Mapper;
import com.example.telefonbozor.validation.Validator;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AbstractService<
        E,
        CD,
        UD,
        D,
        R extends JpaRepository<E,String>,
        V extends Validator<CD,UD>,
        M extends Mapper<E, CD, UD, D>
        > {
    protected final R repository;
    protected final V validator;
    protected final M mapper;

    protected AbstractService(R repository, V validator, M mapper) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
    }
}
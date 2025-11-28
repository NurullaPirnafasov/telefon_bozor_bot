package com.example.telefonbozor.validation;

public interface Validator<CD, UD> {
    void validateCreating(CD dto);
    void validateUpdating(UD dto);
}
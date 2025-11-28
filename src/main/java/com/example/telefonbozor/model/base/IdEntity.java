package com.example.telefonbozor.model.base;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class IdEntity {
    @Id
    private String id = UUID.randomUUID().toString();
}

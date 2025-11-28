package com.example.telefonbozor.model.entity;

import com.example.telefonbozor.model.base.BaseEntity;
import com.example.telefonbozor.model.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement extends BaseEntity {

    private String description;
    private Double price;
    private String brand;
    private String model;
    private String color;
    private String phoneNumber;
    private String phoneMemory;

    @Enumerated(EnumType.STRING)
    private Condition condition;

    private String location;

    @Enumerated(EnumType.STRING)
    private AdStatus status = AdStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AuthUser user;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;
}
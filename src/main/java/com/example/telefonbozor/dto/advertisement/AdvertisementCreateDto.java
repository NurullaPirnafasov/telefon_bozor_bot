package com.example.telefonbozor.dto.advertisement;

import com.example.telefonbozor.model.enums.Condition;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertisementCreateDto {
    private String description;
    private Double price;
    private String brand;
    private String model;
    private String color;
    private String phoneNumber;
    private String phoneMemory;
    private Condition condition;
    private String location;
}

package com.example.telefonbozor.dto.advertisement;

import com.example.telefonbozor.model.enums.AdStatus;
import com.example.telefonbozor.model.enums.Condition;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertisementDto {
    private String id;
    private String description;
    private Double price;
    private String brand;
    private String model;
    private String color;
    private Condition condition;
    private String location;
    private AdStatus status;
    private String phoneNumber;
    private List<String> imageUrls;
}

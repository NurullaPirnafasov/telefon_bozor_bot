package com.example.telefonbozor.mapper;

import com.example.telefonbozor.dto.advertisement.AdvertisementCreateDto;
import com.example.telefonbozor.dto.advertisement.AdvertisementDto;
import com.example.telefonbozor.dto.advertisement.AdvertisementUpdateDto;
import com.example.telefonbozor.model.entity.Advertisement;
import com.example.telefonbozor.model.entity.Image;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdvertisementMapper implements Mapper<Advertisement, AdvertisementCreateDto, AdvertisementUpdateDto, AdvertisementDto> {

    @Override
    public Advertisement toEntity(AdvertisementCreateDto dto) {
        Advertisement advertisement = new Advertisement();
        advertisement.setDescription(dto.getDescription());
        advertisement.setPrice(dto.getPrice());
        advertisement.setBrand(dto.getBrand());
        advertisement.setModel(dto.getModel());
        advertisement.setColor(dto.getColor());
        advertisement.setPhoneNumber(dto.getPhoneNumber());
        advertisement.setPhoneMemory(dto.getPhoneMemory()); // ✔ qo‘shildi
        advertisement.setCondition(dto.getCondition());
        advertisement.setLocation(dto.getLocation());
        return advertisement;
    }

    @Override
    public AdvertisementDto toDto(Advertisement entity) {
        List<String> imageUrls = null;
        if (entity.getImages() != null) {
            imageUrls = entity.getImages().stream()
                    .map(Image::getImageUrl)
                    .collect(Collectors.toList());
        }

        return AdvertisementDto.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .color(entity.getColor())
                .phoneNumber(entity.getPhoneNumber()) // ✔ to‘g‘ri
                .condition(entity.getCondition())
                .location(entity.getLocation())
                .status(entity.getStatus())
                .imageUrls(imageUrls)
                .build();
    }

    @Override
    public void updateEntity(AdvertisementUpdateDto dto, Advertisement entity) {
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getPrice() != null) entity.setPrice(dto.getPrice());
        if (dto.getBrand() != null) entity.setBrand(dto.getBrand());
        if (dto.getModel() != null) entity.setModel(dto.getModel());
        if (dto.getColor() != null) entity.setColor(dto.getColor());
        if (dto.getPhoneNumber() != null) entity.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getPhoneMemory() != null) entity.setPhoneMemory(dto.getPhoneMemory()); // ✔ qo‘shildi
        if (dto.getCondition() != null) entity.setCondition(dto.getCondition());
        if (dto.getLocation() != null) entity.setLocation(dto.getLocation());
    }
}

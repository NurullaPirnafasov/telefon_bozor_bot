package com.example.telefonbozor.mapper;

import com.example.telefonbozor.dto.image.ImageCreateDto;
import com.example.telefonbozor.dto.image.ImageDto;
import com.example.telefonbozor.dto.image.ImageUpdateDto;
import com.example.telefonbozor.model.entity.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper implements Mapper<Image, ImageCreateDto, ImageUpdateDto, ImageDto> {

    @Override
    public Image toEntity(ImageCreateDto dto) {
        return new Image();
    }

    @Override
    public ImageDto toDto(Image entity) {
        return ImageDto.builder()
                .id(entity.getId())
                .imageUrl(entity.getImageUrl())
                .build();
    }

    @Override
    public void updateEntity(ImageUpdateDto dto, Image entity) {
        entity.setImageUrl(dto.getImageUrl());
    }
}

package com.example.telefonbozor.validation;

import com.example.telefonbozor.dto.image.ImageCreateDto;
import com.example.telefonbozor.dto.image.ImageUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class ImageValidator implements Validator<ImageCreateDto, ImageUpdateDto> {

    @Override
    public void validateCreating(ImageCreateDto dto) {

        if (isBlank(dto.getAdvertisementId()))
            throw new RuntimeException("Advertisement ID is required");

        if (dto.getFiles() == null || dto.getFiles().isEmpty())
            throw new RuntimeException("At least one image file must be uploaded");
    }

    @Override
    public void validateUpdating(ImageUpdateDto dto) {

        if (isBlank(dto.getId()))
            throw new RuntimeException("Image ID is required");

        if (dto.getImageUrl() != null && isBlank(dto.getImageUrl()))
            throw new RuntimeException("Image URL cannot be empty");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

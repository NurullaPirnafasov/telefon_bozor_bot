package com.example.telefonbozor.validation;

import com.example.telefonbozor.dto.advertisement.AdvertisementCreateDto;
import com.example.telefonbozor.dto.advertisement.AdvertisementUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class AdvertisementValidator implements Validator<AdvertisementCreateDto, AdvertisementUpdateDto> {

    @Override
    public void validateCreating(AdvertisementCreateDto dto) {

        if (isBlank(dto.getDescription()))
            throw new RuntimeException("Description is required");

        if (dto.getPrice() == null || dto.getPrice() <= 0)
            throw new RuntimeException("Price must be greater than zero");

        if (isBlank(dto.getBrand()))
            throw new RuntimeException("Brand is required");

        if (isBlank(dto.getModel()))
            throw new RuntimeException("Model is required");

        if (dto.getCondition() == null)
            throw new RuntimeException("Condition must be specified");

        if (isBlank(dto.getLocation()))
            throw new RuntimeException("Location is required");

        if (isBlank(dto.getPhoneNumber()))
            throw new RuntimeException("Phone number is required");
    }

    @Override
    public void validateUpdating(AdvertisementUpdateDto dto) {
        if (dto.getPrice() != null && dto.getPrice() <= 0)
            throw new RuntimeException("Price must be greater than zero");

        if (dto.getDescription() != null && isBlank(dto.getDescription()))
            throw new RuntimeException("Description cannot be empty");

        if (dto.getBrand() != null && isBlank(dto.getBrand()))
            throw new RuntimeException("Brand cannot be empty");

        if (dto.getModel() != null && isBlank(dto.getModel()))
            throw new RuntimeException("Model cannot be empty");

        if (dto.getLocation() != null && isBlank(dto.getLocation()))
            throw new RuntimeException("Location cannot be empty");

        if (dto.getPhoneNumber() != null && isBlank(dto.getPhoneNumber()))
            throw new RuntimeException("Phone number cannot be empty");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

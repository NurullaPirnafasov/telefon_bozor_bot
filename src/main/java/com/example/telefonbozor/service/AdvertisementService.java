package com.example.telefonbozor.service;

import com.example.telefonbozor.config.security.CustomUserDetails;
import com.example.telefonbozor.dto.advertisement.AdvertisementCreateDto;
import com.example.telefonbozor.dto.advertisement.AdvertisementDto;
import com.example.telefonbozor.dto.advertisement.AdvertisementUpdateDto;
import com.example.telefonbozor.mapper.AdvertisementMapper;
import com.example.telefonbozor.model.entity.Advertisement;
import com.example.telefonbozor.model.entity.AuthUser;
import com.example.telefonbozor.model.enums.Condition;
import com.example.telefonbozor.repository.AdvertisementRepository;
import com.example.telefonbozor.repository.AuthUserRepository;
import com.example.telefonbozor.service.base.AbstractService;
import com.example.telefonbozor.validation.AdvertisementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementService extends AbstractService<
        Advertisement,
        AdvertisementCreateDto,
        AdvertisementUpdateDto,
        AdvertisementDto,
        AdvertisementRepository,
        AdvertisementValidator,
        AdvertisementMapper> {

    private static final Logger log = LoggerFactory.getLogger(AdvertisementService.class);

    private final AuthUserRepository authUserRepository;

    public AdvertisementService(AdvertisementRepository repository, AdvertisementValidator validator, AdvertisementMapper mapper, AuthUserRepository authUserRepository) {
        super(repository, validator, mapper);
        this.authUserRepository = authUserRepository;
    }

    public AdvertisementDto create(AdvertisementCreateDto dto) {
        validator.validateCreating(dto);

        Advertisement entity = mapper.toEntity(dto);
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        AuthUser validUser = authUserRepository.findByTelegramId(Long.parseLong(userDetails.getUsername()))
                .orElseThrow(() -> {
                    log.error("User not found: {}", userDetails.getUsername());
                    return new RuntimeException("User not found");
                });

        entity.setUser(validUser);
        Advertisement saved = repository.save(entity);
        validUser.setPhoneNumber(dto.getPhoneNumber());
        authUserRepository.save(validUser);
        log.info("Advertisement created with id: {}", saved.getId());
        return mapper.toDto(saved);
    }

    public AdvertisementDto update(AdvertisementUpdateDto dto, String id) {
        log.info("Updating advertisement with id: {}", id);
        validator.validateUpdating(dto);

        Advertisement entity = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Advertisement not found for id: {}", id);
                    return new RuntimeException("Advertisement not found");
                });

        mapper.updateEntity(dto, entity);
        Advertisement saved = repository.save(entity);

        log.info("Advertisement updated with id: {}", saved.getId());
        return mapper.toDto(saved);
    }

    public void delete(String id) {
        log.info("Deleting advertisement with id: {}", id);

        Advertisement entity = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Advertisement not found for deletion, id: {}", id);
                    return new RuntimeException("Advertisement not found");
                });

        entity.setDeleted(true);
        repository.save(entity);

        log.info("Advertisement marked as deleted, id: {}", id);
    }

    public AdvertisementDto get(String id) {
        log.info("Fetching advertisement with id: {}", id);
        Advertisement entity = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Advertisement not found, id: {}", id);
                    return new RuntimeException("Advertisement not found");
                });

        return mapper.toDto(entity);
    }

    public List<AdvertisementDto> getByUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String telegramIdStr;

        if (principal instanceof CustomUserDetails) {
            telegramIdStr = ((CustomUserDetails) principal).getUsername(); // yoki getTelegramId()
        } else if (principal instanceof String) {
            telegramIdStr = (String) principal; // JWT filterdan kelgan username/telegramId
        } else {
            throw new RuntimeException("User not authenticated");
        }

        Long telegramId = Long.parseLong(telegramIdStr);

        AuthUser validUser = authUserRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> {
                    log.error("User not found: {}", telegramIdStr);
                    return new RuntimeException("User not found");
                });

        log.info("Fetching advertisements for user with telegramId: {}", validUser.getTelegramId());

        List<AdvertisementDto> advertisements = repository
                .findAllByTelegramIdAndDeletedFalse(validUser.getTelegramId())
                .stream()
                .map(mapper::toDto)
                .toList();

        log.info("Total advertisements fetched: {}", advertisements.size());
        return advertisements;
    }

    public List<AdvertisementDto> getAll() {
        log.info("Fetching all advertisements");
        List<AdvertisementDto> ads = repository.findAllActiveAds().stream()
                .map(mapper::toDto)
                .toList();
        log.info("Total advertisements fetched: {}", ads.size());
        return ads;
    }

    public List<AdvertisementDto> search(
            Double minPrice,
            Double maxPrice,
            String brand,
            String model,
            String color,
            String phoneMemory,
            Condition condition,
            String location
    ) {
        List<Advertisement> ads = repository.searchAds(
                minPrice,
                maxPrice,
                brand,
                model,
                color,
                phoneMemory,
                condition,
                location
        );

        return ads.stream().map(mapper::toDto).toList();
    }

}

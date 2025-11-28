package com.example.telefonbozor.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.telefonbozor.dto.image.ImageCreateDto;
import com.example.telefonbozor.dto.image.ImageDto;
import com.example.telefonbozor.dto.image.ImageUpdateDto;
import com.example.telefonbozor.mapper.ImageMapper;
import com.example.telefonbozor.model.entity.Advertisement;
import com.example.telefonbozor.model.entity.Image;
import com.example.telefonbozor.repository.AdvertisementRepository;
import com.example.telefonbozor.repository.ImageRepository;
import com.example.telefonbozor.service.base.AbstractService;
import com.example.telefonbozor.validation.ImageValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImageService extends AbstractService<
        Image,
        ImageCreateDto,
        ImageUpdateDto,
        ImageDto,
        ImageRepository,
        ImageValidator,
        ImageMapper> {

    private static final Logger log = LoggerFactory.getLogger(ImageService.class);

    private final AdvertisementRepository advertisementRepository;
    private final Cloudinary cloudinary;

    public ImageService(ImageRepository repository,
                        ImageValidator validator,
                        ImageMapper mapper,
                        AdvertisementRepository advertisementRepository,
                        Cloudinary cloudinary) {
        super(repository, validator, mapper);
        this.advertisementRepository = advertisementRepository;
        this.cloudinary = cloudinary;
    }

    public List<ImageDto> create(ImageCreateDto dto) {
        log.info("Uploading {} images for advertisement id: {}", dto.getFiles().size(), dto.getAdvertisementId());
        List<ImageDto> result = new ArrayList<>();

        Advertisement advertisement = advertisementRepository
                .findByIdAndDeletedFalse(dto.getAdvertisementId())
                .orElseThrow(() -> {
                    log.error("Advertisement not found: {}", dto.getAdvertisementId());
                    return new RuntimeException("Advertisement not found");
                });

        for (MultipartFile file : dto.getFiles()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = uploadResult.get("url").toString();

                Image image = new Image();
                image.setAdvertisement(advertisement);
                image.setImageUrl(imageUrl);

                repository.save(image);
                result.add(mapper.toDto(image));

                log.info("Uploaded image: {}", imageUrl);

            } catch (IOException e) {
                log.error("Failed to upload image: {}", e.getMessage());
                throw new RuntimeException("Failed to upload image: " + e.getMessage());
            }
        }

        log.info("All images uploaded successfully for advertisement id: {}", dto.getAdvertisementId());
        return result;
    }

    public ImageDto update(ImageUpdateDto dto, String id) {
        log.info("Updating image with id: {}", id);
        Image image = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Image not found: {}", id);
                    return new RuntimeException("Image not found");
                });
        mapper.updateEntity(dto, image);
        repository.save(image);
        log.info("Image updated successfully: {}", id);
        return mapper.toDto(image);
    }

    public List<ImageDto> getImagesByAdvertisementId(String adId) {
        log.info("Fetching images for advertisement id: {}", adId);
        List<Image> images = repository.findByAdvertisementId(adId);
        List<ImageDto> result = new ArrayList<>();
        for (Image img : images) {
            result.add(mapper.toDto(img));
        }
        log.info("Fetched {} images for advertisement id: {}", result.size(), adId);
        return result;
    }

    public void delete(String id) {
        log.info("Deleting image with id: {}", id);
        repository.deleteById(id);
        log.info("Image deleted successfully: {}", id);
    }

    public ImageDto get(String id) {
        log.info("Fetching image with id: {}", id);
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> {
                    log.error("Image not found: {}", id);
                    return new RuntimeException("Image not found");
                });
    }

    public List<ImageDto> getAll() {
        log.info("Fetching all images");
        List<ImageDto> result = new ArrayList<>();
        for (Image image : repository.findAll()) {
            result.add(mapper.toDto(image));
        }
        log.info("Total images fetched: {}", result.size());
        return result;
    }
}

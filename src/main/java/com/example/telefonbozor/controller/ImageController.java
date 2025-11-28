package com.example.telefonbozor.controller;

import com.example.telefonbozor.dto.image.ImageCreateDto;
import com.example.telefonbozor.dto.image.ImageDto;
import com.example.telefonbozor.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<List<ImageDto>> uploadImages(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("adId") String adId) {

        ImageCreateDto dto = ImageCreateDto.builder()
                .advertisementId(adId)
                .files(files)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(imageService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable String id) {
        imageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{adId}")
    public ResponseEntity<List<ImageDto>> getImagesByAd(@PathVariable String adId) {
        return ResponseEntity.ok(imageService.getImagesByAdvertisementId(adId));
    }
}

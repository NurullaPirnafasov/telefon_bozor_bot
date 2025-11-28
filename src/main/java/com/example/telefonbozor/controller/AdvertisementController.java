package com.example.telefonbozor.controller;

import com.example.telefonbozor.dto.advertisement.AdvertisementCreateDto;
import com.example.telefonbozor.dto.advertisement.AdvertisementDto;
import com.example.telefonbozor.dto.advertisement.AdvertisementUpdateDto;
import com.example.telefonbozor.model.enums.Condition;
import com.example.telefonbozor.service.AdvertisementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ads")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @PostMapping
    public ResponseEntity<AdvertisementDto> create(@RequestBody AdvertisementCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(advertisementService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(advertisementService.get(id));
    }

    @GetMapping("/my-ads")
    public ResponseEntity<List<AdvertisementDto>> getAllByUser() {
        return ResponseEntity.ok(advertisementService.getByUser());
    }

    @GetMapping("/search")
    public ResponseEntity<List<AdvertisementDto>> searchAds(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String phoneMemory,
            @RequestParam(required = false) Condition condition,
            @RequestParam(required = false) String location
    ) {
        List<AdvertisementDto> ads = advertisementService.search(
                minPrice, maxPrice, brand, model, color, phoneMemory, condition, location
        );

        if (ads.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 agar natija bo'lmasa
        }
        return ResponseEntity.ok(ads); // 200 + natijalar
    }

    @GetMapping
    public ResponseEntity<List<AdvertisementDto>> getAll() {
        return ResponseEntity.ok(advertisementService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdvertisementDto> update(
            @PathVariable String id,
            @RequestBody AdvertisementUpdateDto dto
    ) {
        return ResponseEntity.ok(advertisementService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        advertisementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.example.telefonbozor.repository;

import com.example.telefonbozor.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findByAdvertisementId(String adId);
}

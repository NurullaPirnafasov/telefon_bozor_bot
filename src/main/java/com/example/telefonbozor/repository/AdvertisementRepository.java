package com.example.telefonbozor.repository;

import com.example.telefonbozor.model.entity.Advertisement;
import com.example.telefonbozor.model.enums.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, String> {
    Optional<Advertisement> findByIdAndDeletedFalse(String id);

    @Query("""
                SELECT DISTINCT a FROM Advertisement a
                LEFT JOIN FETCH a.images
                JOIN FETCH a.user
                WHERE a.deleted = false
                  AND a.status = com.example.telefonbozor.model.enums.AdStatus.ACTIVE
            """)
    List<Advertisement> findAllActiveAds();


    @Query("""
                SELECT DISTINCT a FROM Advertisement a
                LEFT JOIN FETCH a.images
                JOIN FETCH a.user
                WHERE a.user.telegramId = :telegramId
                AND a.deleted = false
            """)
    List<Advertisement> findAllByTelegramIdAndDeletedFalse(@Param("telegramId") Long telegramId);

    @Query("SELECT a FROM Advertisement a WHERE " +
            "(:minPrice IS NULL OR a.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR a.price <= :maxPrice) AND " +
            "(:brand IS NULL OR LOWER(a.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
            "(:model IS NULL OR LOWER(a.model) LIKE LOWER(CONCAT('%', :model, '%'))) AND " +
            "(:color IS NULL OR LOWER(a.color) LIKE LOWER(CONCAT('%', :color, '%'))) AND " +
            "(:phoneMemory IS NULL OR LOWER(a.phoneMemory) LIKE LOWER(CONCAT('%', :phoneMemory, '%'))) AND " +
            "(:condition IS NULL OR a.condition = :condition) AND " +
            "(:location IS NULL OR LOWER(a.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "a.status = com.example.telefonbozor.model.enums.AdStatus.ACTIVE")
    List<Advertisement> searchAds(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("brand") String brand,
            @Param("model") String model,
            @Param("color") String color,
            @Param("phoneMemory") String phoneMemory,
            @Param("condition") Condition condition,
            @Param("location") String location
    );

}

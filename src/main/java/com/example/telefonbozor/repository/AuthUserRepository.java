package com.example.telefonbozor.repository;

import com.example.telefonbozor.model.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, String> {
    @Query("select a from AuthUser a where a.id = :id and a.deleted=false")
    Optional<AuthUser> findById(@Param("id") String id);


    Optional<AuthUser> findByTelegramId(Long telegramId);
}

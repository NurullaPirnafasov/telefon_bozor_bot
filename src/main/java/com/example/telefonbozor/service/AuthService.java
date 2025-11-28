package com.example.telefonbozor.service;

import com.example.telefonbozor.config.security.JwtUtil;
import com.example.telefonbozor.model.entity.AuthUser;
import com.example.telefonbozor.model.enums.Role;
import com.example.telefonbozor.repository.AuthUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final AuthUserRepository repository;
    private final JwtUtil jwtUtil;

    public AuthService(AuthUserRepository repository, JwtUtil jwtUtil) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public String loginOrRegisterTelegramUser(Long telegramId, String fullName) {
        log.info("Attempting login/register for Telegram user: {} ({})", fullName, telegramId);

        Optional<AuthUser> optionalUser = repository.findByTelegramId(telegramId);

        AuthUser user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            log.info("Existing Telegram user found: {}", telegramId);
        } else {
            user = AuthUser.builder()
                    .telegramId(telegramId)
                    .fullName(fullName)
                    .role(Role.USER)
                    .build();
            repository.save(user);
            log.info("New Telegram user registered: {} ({})", fullName, telegramId);
        }

        String username = String.valueOf(user.getTelegramId());
        String role= String.valueOf(user.getRole());
        String token = jwtUtil.generateToken(username,role);

        log.info("JWT token generated for user: {}", username);
        return token;
    }
}

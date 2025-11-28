package com.example.telefonbozor.controller;

import com.example.telefonbozor.service.AuthService;
import com.example.telefonbozor.validation.TelegramInitDataValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
public class TelegramLoginController {

    private final AuthService authUserService;
    private final TelegramInitDataValidator validator;

    public TelegramLoginController(AuthService authUserService, TelegramInitDataValidator validator) {
        this.authUserService = authUserService;
        this.validator = validator;
    }

    @PostMapping("/telegram")
    public ResponseEntity<Map<String, String>> telegramLogin(@RequestBody Map<String, String> body) {

        String initData = body.get("initData");
        if (!validator.isValid(initData)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid initData"));
        }

        Long telegramId = validator.getTelegramId(initData);
        String fullName = validator.getFullName(initData);

        String token = authUserService.loginOrRegisterTelegramUser(telegramId, fullName);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/telegram/widget")
    public ResponseEntity<Map<String,String>> telegramWidgetLogin(@RequestBody Map<String, String> payload) {
        boolean valid = validator.isValidWidgetData(payload);

        if (!valid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "auth_failed"));
        }

        String telegramId = payload.get("id");
        String fullName = payload.get("first_name") +
                (payload.get("last_name") != null ? " " + payload.get("last_name") : "");

        String token = authUserService.loginOrRegisterTelegramUser(Long.valueOf(telegramId), fullName);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
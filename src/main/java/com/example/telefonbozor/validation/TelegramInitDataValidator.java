package com.example.telefonbozor.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component
public class TelegramInitDataValidator {

    private static final Logger log = LoggerFactory.getLogger(TelegramInitDataValidator.class);

    private final String botToken;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final long MAX_AUTH_DATE_DIFF = 3600; // 1 soat

    public TelegramInitDataValidator(@Value("${bot.token}") String botToken) {
        this.botToken = botToken;
        log.info("TelegramInitDataValidator initialized with bot token: {}", maskToken(botToken));
    }
    public boolean isValidWidgetData(Map<String, String> data) {
        try {
            String checkHash = data.get("hash");
            if (checkHash == null) return false;

            // Hash uchun mapni tartiblaymiz va 'hash'ni olib tashlaymiz
            SortedMap<String, String> sorted = new TreeMap<>(data);
            sorted.remove("hash");

            // Data string yaratish: key=value ko'rinishida, alfavit bo'yicha
            StringBuilder dataString = new StringBuilder();
            for (Map.Entry<String, String> entry : sorted.entrySet()) {
                String key = entry.getKey();
                String value = URLDecoder.decode(entry.getValue(), StandardCharsets.UTF_8); // URL decode
                dataString.append(key).append("=").append(value).append("\n");
            }
            // Oxirgi '\n'ni olib tashlash
            if (dataString.length() > 0) dataString.setLength(dataString.length() - 1);

            // HMAC-SHA256 hisoblash (kalit = SHA256(botToken))
            byte[] secretKey = MessageDigest.getInstance("SHA-256").digest(botToken.getBytes(StandardCharsets.UTF_8));
            Mac sha256 = Mac.getInstance("HmacSHA256");
            sha256.init(new SecretKeySpec(secretKey, "HmacSHA256"));

            byte[] hashBytes = sha256.doFinal(dataString.toString().getBytes(StandardCharsets.UTF_8));
            String calculatedHash = DatatypeConverter.printHexBinary(hashBytes).toLowerCase();

            return calculatedHash.equals(checkHash);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean isValid(String initData) {
        log.debug("Validating initData: {}", initData);

        if (initData == null || initData.isBlank()) {
            log.warn("initData is null or blank");
            return false;
        }

        try {
            Map<String, String> parsed = parseInitData(initData);
            String hash = parsed.remove("hash");
            if (hash == null) {
                log.warn("Missing 'hash' parameter in initData");
                return false;
            }

            String dataCheckString = buildDataCheckString(parsed);

            // 1. Secret key = HMAC_SHA256(botToken, "WebAppData")
            byte[] secretKey = hmacSha256Raw(
                    botToken.getBytes(StandardCharsets.UTF_8),
                    "WebAppData".getBytes(StandardCharsets.UTF_8)
            );

            // 2. Calculated hash = HMAC_SHA256(dataCheckString, secretKey)
            byte[] calculatedHashBytes = hmacSha256Raw(
                    dataCheckString.getBytes(StandardCharsets.UTF_8),
                    secretKey
            );
            String calculatedHashHex = bytesToHex(calculatedHashBytes);

            boolean hashValid = calculatedHashHex.equalsIgnoreCase(hash);
            log.info("Hash validation: {}", hashValid ? "SUCCESS" : "FAILED");

            // 3. auth_date tekshirish
            String authDateStr = parsed.get("auth_date");
            if (authDateStr == null) {
                log.warn("Missing 'auth_date' in initData");
                return false;
            }

            long authDate = Long.parseLong(authDateStr);
            long now = System.currentTimeMillis() / 1000;
            long diff = now - authDate;

            boolean timeValid = diff <= MAX_AUTH_DATE_DIFF;
            log.info("auth_date: {} ({} seconds ago) → Time valid: {}", authDate, diff, timeValid);

            return hashValid && timeValid;

        } catch (Exception e) {
            log.error("Validation failed due to exception", e);
            return false;
        }
    }

    public Long getTelegramId(String initData) {
        try {
            Map<String, String> parsed = parseInitData(initData);
            String userJson = parsed.get("user");
            if (userJson == null) return null;

            JsonNode userNode = objectMapper.readTree(userJson);
            return userNode.get("id").asLong();
        } catch (Exception e) {
            log.error("Failed to extract Telegram ID", e);
            return null;
        }
    }

    public String getFullName(String initData) {
        try {
            Map<String, String> parsed = parseInitData(initData);
            String userJson = parsed.get("user");
            if (userJson == null) return null;

            JsonNode userNode = objectMapper.readTree(userJson);
            String firstName = userNode.has("first_name") ? userNode.get("first_name").asText() : "";
            String lastName = userNode.has("last_name") ? userNode.get("last_name").asText() : "";
            String fullName = (firstName + " " + lastName).trim();
            return fullName.isEmpty() ? null : fullName;
        } catch (Exception e) {
            log.error("Failed to extract full name", e);
            return null;
        }
    }

    // ======================= Helper methods =======================

    private Map<String, String> parseInitData(String initData) {
        Map<String, String> result = new HashMap<>();
        for (String pair : initData.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                String key = parts[0];
                String value = java.net.URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                result.put(key, value);
            }
        }
        return result;
    }

    private String buildDataCheckString(Map<String, String> data) {
        return data.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");
    }

    private byte[] hmacSha256Raw(byte[] data, byte[] key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
            mac.init(secretKeySpec);
            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("HMAC calculation failed", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private String maskToken(String token) {
        if (token == null || token.length() <= 8) return "****";
        return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
    }

}

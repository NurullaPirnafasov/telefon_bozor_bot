package com.example.telefonbozor.service;

import com.example.telefonbozor.model.entity.Advertisement;
import com.example.telefonbozor.model.enums.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramMessageService {

    private final RestTemplate restTemplate;

    @Value("${bot.token}")
    private String botToken;

    private String getSendMediaGroupUrl() {
        return "https://api.telegram.org/bot" + botToken + "/sendMediaGroup";
    }

    public void sendAlbum(Long chatId, Advertisement ad, List<String> imageUrls) {

        String caption = """
                📢 #%s

                📲 %s %s
                💾 Xotirasi   %s
                🎨 Rangi  %s
                ️ Holati %s
                📦📄 Pasport nusxa beriladi
                💰 Narxi %s so'm
                📞 Tel %s
                🇺🇿 #%s
                %s
                """.formatted(
                ad.getBrand().toLowerCase(),
                ad.getBrand(),
                ad.getModel(),
                ad.getPhoneMemory(),
                ad.getColor(),
                ad.getCondition() == Condition.NEW ? "yangi" : "ishlatilgan",
                ad.getPrice(),
                ad.getPhoneNumber(),
                ad.getLocation(),
                ad.getDescription()
        );

        StringBuilder mediaArray = new StringBuilder("[");
        for (int i = 0; i < imageUrls.size(); i++) {
            String img = imageUrls.get(i);

            if (i == 0) {
                mediaArray.append("""
                        {
                          "type": "photo",
                          "media": "%s",
                          "caption": "%s",
                          "parse_mode": "HTML"
                        }
                        """.formatted(img, caption));
            } else {
                mediaArray.append("""
                        {
                          "type": "photo",
                          "media": "%s"
                        }
                        """.formatted(img));
            }

            if (i != imageUrls.size() - 1) mediaArray.append(",");
        }
        mediaArray.append("]");

        String body = """
                {
                  "chat_id": %d,
                  "media": %s
                }
                """.formatted(chatId, mediaArray.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        restTemplate.postForObject(getSendMediaGroupUrl(), request, String.class);
    }
}

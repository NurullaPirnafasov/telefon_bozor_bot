package com.example.telefonbozor.controller;

import com.example.telefonbozor.model.TelegramUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telegram")
public class TelegramController {

    @PostMapping("/webhook")
    public ResponseEntity<String> onUpdateReceived(@RequestBody TelegramUpdate update){
        if(update.getMessage() != null) {
            String chatId = update.getMessage().getChat().getId().toString();
            String text = update.getMessage().getText();
            System.out.println("ChatId: " + chatId + ", Text: " + text);

            // Shu yerda xabarni qayta ishlash logikasi
        }
        return ResponseEntity.ok("ok");
    }
}
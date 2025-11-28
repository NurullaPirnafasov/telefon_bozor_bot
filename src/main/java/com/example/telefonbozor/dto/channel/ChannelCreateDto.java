package com.example.telefonbozor.dto.channel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelCreateDto {
    private String name;
    private String ownerId;
    private Long telegramChannelId;
    private String link;
}

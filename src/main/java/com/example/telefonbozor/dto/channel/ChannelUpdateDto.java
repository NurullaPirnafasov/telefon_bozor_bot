package com.example.telefonbozor.dto.channel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelUpdateDto {
    private String name;
    private Long telegramChannelId;
    private String link;
}

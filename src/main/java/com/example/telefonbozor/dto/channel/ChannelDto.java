package com.example.telefonbozor.dto.channel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelDto {
    private String id;
    private String name;
    private Long telegramChannelId;
    private String link;
    private boolean active;
    private String ownerId;
}

package com.example.telefonbozor.validation;

import com.example.telefonbozor.dto.channel.ChannelCreateDto;
import com.example.telefonbozor.dto.channel.ChannelUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class ChannelValidator implements Validator<
        ChannelCreateDto,
        ChannelUpdateDto> {

    @Override
    public void validateCreating(ChannelCreateDto dto) {
        if (isBlank(dto.getName()))
            throw new RuntimeException("Channel name is required");

        if (isBlank(dto.getOwnerId()))
            throw new RuntimeException("Owner ID is required");

        if (dto.getTelegramChannelId() == null) {
            throw new RuntimeException("Telegram channel ID must be a valid channel chat_id");
        }
    }

    @Override
    public void validateUpdating(ChannelUpdateDto dto) {
        if (dto.getName() != null && isBlank(dto.getName()))
            throw new RuntimeException("Channel name cannot be empty");

        if (dto.getTelegramChannelId() != null && dto.getTelegramChannelId() <= 0)
            throw new RuntimeException("Telegram channel ID must be a valid number");

        if (dto.getLink() != null && isBlank(dto.getLink()))
            throw new RuntimeException("Channel link cannot be empty");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

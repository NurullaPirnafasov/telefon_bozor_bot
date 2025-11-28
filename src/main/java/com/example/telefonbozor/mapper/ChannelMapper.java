package com.example.telefonbozor.mapper;

import com.example.telefonbozor.dto.channel.ChannelCreateDto;
import com.example.telefonbozor.dto.channel.ChannelDto;
import com.example.telefonbozor.dto.channel.ChannelUpdateDto;
import com.example.telefonbozor.model.entity.AuthUser;
import com.example.telefonbozor.model.entity.Channel;
import com.example.telefonbozor.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper implements Mapper<
        Channel,
        ChannelCreateDto,
        ChannelUpdateDto,
        ChannelDto> {

    private final AuthUserRepository authUserRepository;

    @Override
    public Channel toEntity(ChannelCreateDto dto) {
        AuthUser owner = authUserRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Channel channel = new Channel();
        channel.setName(dto.getName());
        channel.setOwner(owner);
        channel.setTelegramChannelId(dto.getTelegramChannelId());
        channel.setLink(dto.getLink());
        channel.setActive(true);
        return channel;
    }

    @Override
    public ChannelDto toDto(Channel entity) {
        ChannelDto dto = new ChannelDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setTelegramChannelId(entity.getTelegramChannelId());
        dto.setLink(entity.getLink());
        dto.setActive(entity.isActive());
        dto.setOwnerId(entity.getOwner().getId()); // ✔ to‘g‘ri
        return dto;
    }

    @Override
    public void updateEntity(ChannelUpdateDto dto, Channel entity) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getTelegramChannelId() != null) entity.setTelegramChannelId(dto.getTelegramChannelId());
        if (dto.getLink() != null) entity.setLink(dto.getLink());
    }
}

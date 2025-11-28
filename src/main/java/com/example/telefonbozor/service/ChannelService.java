package com.example.telefonbozor.service;

import com.example.telefonbozor.dto.channel.ChannelCreateDto;
import com.example.telefonbozor.dto.channel.ChannelDto;
import com.example.telefonbozor.dto.channel.ChannelUpdateDto;
import com.example.telefonbozor.mapper.ChannelMapper;
import com.example.telefonbozor.model.entity.Advertisement;
import com.example.telefonbozor.model.entity.Channel;
import com.example.telefonbozor.model.entity.Image;
import com.example.telefonbozor.model.enums.AdStatus;
import com.example.telefonbozor.repository.AdvertisementRepository;
import com.example.telefonbozor.repository.ChannelRepository;
import com.example.telefonbozor.service.base.AbstractService;
import com.example.telefonbozor.validation.ChannelValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService extends AbstractService<
        Channel,
        ChannelCreateDto,
        ChannelUpdateDto,
        ChannelDto,
        ChannelRepository,
        ChannelValidator,
        ChannelMapper> {

    private final AdvertisementRepository advertisementRepository;
    private final TelegramMessageService messageService;

    public ChannelService(ChannelRepository repository,
                          ChannelValidator validator,
                          ChannelMapper mapper, AdvertisementRepository advertisementRepository, TelegramMessageService messageService) {
        super(repository, validator, mapper);
        this.advertisementRepository = advertisementRepository;
        this.messageService = messageService;
    }

    public ChannelDto save(ChannelCreateDto dto) {
        validator.validateCreating(dto);

        Channel channel = mapper.toEntity(dto);

        repository.save(channel);

        return mapper.toDto(channel);
    }

    public List<ChannelDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<ChannelDto> getAllActive() {
        return repository.findAllByActiveTrue()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public ChannelDto getById(String id) {
        Channel channel = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        return mapper.toDto(channel);
    }

    public ChannelDto update(String id, ChannelUpdateDto dto) {
        validator.validateUpdating(dto);

        Channel channel = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        mapper.updateEntity(dto, channel);

        repository.save(channel);

        return mapper.toDto(channel);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public ChannelDto toggleActive(String id) {
        Channel channel = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        channel.setActive(!channel.isActive());

        repository.save(channel);

        return mapper.toDto(channel);
    }

    public void sendAdvertisementToChannels(String adId, List<String> channelIds) {
        Advertisement ad = advertisementRepository.findByIdAndDeletedFalse(adId)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        if (ad.getStatus() != AdStatus.ACTIVE) {
            throw new RuntimeException("Payment required to send this ad");
        }

        List<Channel> channels = repository.findAllById(channelIds).stream()
                .filter(Channel::isActive)
                .toList();
        List<String> imageUrls=ad.getImages().stream()
                .map(Image::getImageUrl)
                .toList();
        for (Channel channel : channels) {
            messageService.sendAlbum(channel.getTelegramChannelId(),ad,imageUrls);
        }
    }
}

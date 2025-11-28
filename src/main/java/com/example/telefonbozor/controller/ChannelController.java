package com.example.telefonbozor.controller;

import com.example.telefonbozor.dto.channel.ChannelCreateDto;
import com.example.telefonbozor.dto.channel.ChannelDto;
import com.example.telefonbozor.dto.channel.ChannelUpdateDto;
import com.example.telefonbozor.service.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ChannelDto> create(@RequestBody ChannelCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.save(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ChannelDto>> getAll() {
        return ResponseEntity.ok(channelService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ChannelDto>> getAllActiveChannels() {
        return ResponseEntity.ok(channelService.getAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChannelDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(channelService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ChannelDto> update(@PathVariable String id,
                                             @RequestBody ChannelUpdateDto dto) {
        return ResponseEntity.ok(channelService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        channelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/toggle")
    public ResponseEntity<ChannelDto> toggle(@PathVariable String id) {
        return ResponseEntity.ok(channelService.toggleActive(id));
    }

    @PostMapping("/send-ad/{adId}")
    public ResponseEntity<String> sendAd(@PathVariable String adId, @RequestBody List<String> channelIds) {
        channelService.sendAdvertisementToChannels(adId, channelIds);
        return ResponseEntity.ok("Advertisement sent to channels");
    }
}

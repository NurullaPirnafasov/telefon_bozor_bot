package com.example.telefonbozor.repository;

import com.example.telefonbozor.model.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel,String> {
    List<Channel> findAllByActiveTrue();
}

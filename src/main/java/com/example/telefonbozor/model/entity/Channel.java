package com.example.telefonbozor.model.entity;

import com.example.telefonbozor.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Channel extends BaseEntity {

    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private AuthUser owner;
    private Long telegramChannelId;
    @Column(length = 500)
    private String link;

    private boolean active = true;
}

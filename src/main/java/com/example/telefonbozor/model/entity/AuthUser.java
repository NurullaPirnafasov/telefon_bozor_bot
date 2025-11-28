package com.example.telefonbozor.model.entity;

import com.example.telefonbozor.model.base.BaseEntity;
import com.example.telefonbozor.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUser extends BaseEntity {

    @Column(unique = true)
    private Long telegramId;
    private String fullName;
    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role=Role.USER;
}

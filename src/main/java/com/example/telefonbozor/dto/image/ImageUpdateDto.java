package com.example.telefonbozor.dto.image;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageUpdateDto {
    private String id;
    private String imageUrl;
}

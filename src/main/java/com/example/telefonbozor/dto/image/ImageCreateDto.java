package com.example.telefonbozor.dto.image;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageCreateDto {
    private String advertisementId;
    private List<MultipartFile> files;
}

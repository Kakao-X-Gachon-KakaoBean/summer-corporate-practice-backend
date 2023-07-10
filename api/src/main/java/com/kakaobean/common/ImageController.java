package com.kakaobean.common;

import com.kakaobean.independentlysystem.image.ImageService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/images")
    public ResponseEntity<ImageResponseDto> uploadImage(@RequestParam MultipartFile image) throws IOException {
        String imageUrl = imageService.upload(image);
        return ResponseEntity.ok(new ImageResponseDto(imageUrl));
    }
}

package com.ec2test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProfileImageUrlResponseDto {

    private Long memberId;
    private String presignedUrl;
    private LocalDateTime expiresAt;
}

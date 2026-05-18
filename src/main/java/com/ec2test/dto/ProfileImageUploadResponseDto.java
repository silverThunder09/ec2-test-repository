package com.ec2test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileImageUploadResponseDto {

    private Long memberId;
    private String profileImageKey;
}

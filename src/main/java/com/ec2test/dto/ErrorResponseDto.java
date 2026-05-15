package com.ec2test.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        LocalDateTime timestamp,
        int status,
        String message
) {

    public static ErrorResponseDto of(int status, String message) {
        return new ErrorResponseDto(LocalDateTime.now(), status, message);
    }
}

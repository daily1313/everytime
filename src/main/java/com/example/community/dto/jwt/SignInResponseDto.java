package com.example.community.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponseDto {
    private String accessToken;
}

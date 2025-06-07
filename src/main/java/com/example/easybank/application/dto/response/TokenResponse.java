package com.example.easybank.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {
    private String token;
    @Builder.Default
    private String tokenType = "Bearer";
}

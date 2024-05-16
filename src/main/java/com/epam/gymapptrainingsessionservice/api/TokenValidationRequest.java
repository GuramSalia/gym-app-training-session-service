package com.epam.gymapptrainingsessionservice.api;

import lombok.Data;

@Data
public class TokenValidationRequest {
    private String token;

    public TokenValidationRequest(String token) {
        this.token = token;
    }

    public TokenValidationRequest() {}
}

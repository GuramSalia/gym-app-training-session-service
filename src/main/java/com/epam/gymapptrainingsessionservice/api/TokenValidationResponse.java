package com.epam.gymapptrainingsessionservice.api;

import lombok.Data;

@Data
public class TokenValidationResponse {
    private boolean tokenIsValid;

    public TokenValidationResponse(boolean tokenIsValid) {
        this.tokenIsValid = tokenIsValid;
    }

    public TokenValidationResponse() {}
}

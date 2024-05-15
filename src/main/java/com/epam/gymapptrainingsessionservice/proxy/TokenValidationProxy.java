package com.epam.gymapptrainingsessionservice.proxy;

import com.epam.gymapptrainingsessionservice.api.TokenValidationRequest;
import com.epam.gymapptrainingsessionservice.api.TokenValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "main-service")
public interface TokenValidationProxy {

    @GetMapping("/gym-app/public/token-validation")
    public ResponseEntity<TokenValidationResponse> validateToken(
            @RequestBody TokenValidationRequest tokenValidationRequest,
            @RequestHeader(name = "gym-app-correlation-id", required = false, defaultValue = "no-correlation-id") String correlationId
    );
}

package com.epam.gymapptrainingsessionservice.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Date;

@Data
public class UpdateStatRequest {
    @NotNull
    private Integer trainerId;
    @NotNull
    private Integer year;
    @NotNull
    private Integer month;
    @NotNull
    private Integer duration;
    @NotNull
    private ActionType actionType;
    @NotNull
    private String token;
}

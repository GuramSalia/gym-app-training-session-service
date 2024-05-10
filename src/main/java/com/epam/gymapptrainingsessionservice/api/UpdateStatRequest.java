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
    private Date trainingDate;
    @NotNull
    private Integer duration;
    @NotNull
    private ActionType actionType;
}

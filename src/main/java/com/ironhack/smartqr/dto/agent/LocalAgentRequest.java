package com.ironhack.smartqr.dto.agent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocalAgentRequest( @NotBlank(message = "Question is required")
                                 @Size(max = 500, message = "Question cannot exceed 500 characters") String question
) {
}

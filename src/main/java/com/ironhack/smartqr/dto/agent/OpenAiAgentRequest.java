package com.ironhack.smartqr.dto.agent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OpenAiAgentRequest(

        @NotBlank(message = "Conversation id is required.")
        @Size(max = 100, message = "Conversation id cannot exceed 100 characters.")
        String conversationId,

        @NotBlank(message = "Question is required.")
        @Size(max = 1000, message = "Question cannot exceed 1000 characters.")
        String question
) {
}
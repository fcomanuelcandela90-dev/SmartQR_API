package com.ironhack.smartqr.dto.agent;

public record OpenAiAgentResponse(String conversationId, String question, String answer, String model, String memoryType
) {
}
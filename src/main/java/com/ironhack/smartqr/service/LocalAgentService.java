package com.ironhack.smartqr.service;

import com.ironhack.smartqr.dto.agent.LocalAgentRequest;
import com.ironhack.smartqr.dto.agent.LocalAgentResponse;
import com.ironhack.smartqr.tool.AdminAgentTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class LocalAgentService {

    private static final String LOCAL_MODEL_NAME = "qwen3:4b";

    private final ChatClient localOllamaChatClient;
    private final AdminAgentTools adminAgentTools;

    public LocalAgentService(
            @Qualifier("localOllamaChatClient") ChatClient localOllamaChatClient,
            AdminAgentTools adminAgentTools
    ) {
        this.localOllamaChatClient = localOllamaChatClient;
        this.adminAgentTools = adminAgentTools;
    }

    public LocalAgentResponse askLocalAgent(LocalAgentRequest request) {
        String answer = localOllamaChatClient.prompt()
                .system("""
                    You are the internal administrative assistant for SmartQR restaurant management.

                    Rules:
                    - Reply in Spanish.
                    - Use the available tools before answering questions about sales, income, orders, kitchen workload or menu products.
                    - You only have read-only access to operational information.
                    - Never claim that you created, modified, deleted or confirmed any data.
                    - If the administrator asks you to modify data, explain that the local agent is read-only.
                    - Give a short and clear answer suitable for a restaurant administrator.
                    """)
                .user(request.question())
                .tools(adminAgentTools)
                .call()
                .content();

        if (answer == null || answer.isBlank()) {
            throw new IllegalStateException(
                    "The local Ollama agent did not return a response."
            );
        }

        return new LocalAgentResponse(
                request.question(),
                answer.trim(),
                LOCAL_MODEL_NAME
        );
    }

}

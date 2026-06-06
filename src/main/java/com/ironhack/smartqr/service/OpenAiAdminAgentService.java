package com.ironhack.smartqr.service;

import com.ironhack.smartqr.dto.agent.OpenAiAgentRequest;
import com.ironhack.smartqr.dto.agent.OpenAiAgentResponse;
import com.ironhack.smartqr.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Slf4j
@Service
public class OpenAiAdminAgentService {

    private static final String OPENAI_MODEL_NAME = "gpt-4o-mini";
    private static final String MEMORY_TYPE = "JdbcChatMemoryRepository + MessageWindowChatMemory";

    private final ChatClient openAiMemoryChatClient;
    private final DashboardService dashboardService;
    private final OrderService orderService;
    private final ProductService productService;

    public OpenAiAdminAgentService(
            @Qualifier("openAiMemoryChatClient") ChatClient openAiMemoryChatClient,
            DashboardService dashboardService,
            OrderService orderService,
            ProductService productService
    ) {
        this.openAiMemoryChatClient = openAiMemoryChatClient;
        this.dashboardService = dashboardService;
        this.orderService = orderService;
        this.productService = productService;
    }

    public OpenAiAgentResponse askOpenAiAgent(OpenAiAgentRequest request) {
        String answer;
        String operationalContext = buildOperationalContext();

        try {
            answer = openAiMemoryChatClient.prompt()
                    .system("""
                            Eres el asistente administrativo OpenAI de SmartQR.

                            Reglas:
                            - Responde siempre en español.
                            - Usa el contexto operativo que recibe la petición.
                            - El contexto es de solo lectura.
                            - No digas que has creado, modificado, eliminado o confirmado datos.
                            - Si el administrador pide modificar datos, explica que este chat solo puede consultar y razonar.
                            - Usa la memoria de conversación para entender preguntas de seguimiento.
                            - Si todavía no hay datos suficientes, dilo de forma clara.
                            - Responde de forma breve, útil y realista para una persona que administra un restaurante.
                            """)
                    .user("""
                            Contexto operativo actual de SmartQR:

                            %s

                            Pregunta del administrador:
                            %s
                            """.formatted(operationalContext, request.question()))
                    .advisors(advisorSpec ->
                            advisorSpec.param(
                                    ChatMemory.CONVERSATION_ID,
                                    request.conversationId()
                            )
                    )
                    .call()
                    .content();

        } catch (Exception exception) {
            log.error(
                    "OpenAI admin agent failed. conversationId={}",
                    request.conversationId(),
                    exception
            );

            throw new ExternalServiceException(
                    "OpenAI admin agent service is currently unavailable.",
                    exception
            );
        }

        if (answer == null || answer.isBlank()) {
            throw new ExternalServiceException(
                    "The OpenAI admin agent did not return a response."
            );
        }

        return new OpenAiAgentResponse(
                request.conversationId(),
                request.question(),
                answer.trim(),
                OPENAI_MODEL_NAME,
                MEMORY_TYPE
        );
    }

    private String buildOperationalContext() {
        String dashboardMetrics = safeToText(
                "dashboardMetrics",
                dashboardService::getDashboardMetrics
        );

        String kitchenQueue = safeToText(
                "kitchenQueue",
                orderService::getKitchenQueue
        );

        String availableMenu = safeToText(
                "availableMenu",
                productService::getAvailableMenu
        );

        return """
                Dashboard metrics:
                %s

                Kitchen queue:
                %s

                Available menu:
                %s
                """.formatted(
                dashboardMetrics,
                kitchenQueue,
                availableMenu
        );
    }

    private String safeToText(String contextName, Supplier<?> supplier) {
        try {
            Object data = supplier.get();

            if (data == null) {
                return contextName + ": no data available";
            }

            return data.toString();

        } catch (Exception exception) {
            log.warn(
                    "Could not load OpenAI admin agent context: {}",
                    contextName,
                    exception
            );

            return contextName + ": not available";
        }
    }
}
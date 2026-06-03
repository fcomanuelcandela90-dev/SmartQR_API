package com.ironhack.smartqr.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaConfig {

    @Bean(name = "localOllamaChatModel")
    public OllamaChatModel localOllamaChatModel() {
        OllamaApi ollamaApi = OllamaApi.builder().build();

        return OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(
                        OllamaChatOptions.builder()
                                .model("qwen3:4b")
                                .temperature(0.2)
                                .build()
                )
                .build();
    }

    @Bean(name = "localOllamaChatClient")
    public ChatClient localOllamaChatClient(
            @Qualifier("localOllamaChatModel") OllamaChatModel localOllamaChatModel //en esta anotación se indica que el cliente local se construye con el modelo local de ollama.
    ) {
        return ChatClient.create(localOllamaChatModel);
    }
}

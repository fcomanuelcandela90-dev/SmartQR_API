package com.ironhack.smartqr.controller;

import com.ironhack.smartqr.dto.agent.OpenAiAgentRequest;
import com.ironhack.smartqr.dto.agent.OpenAiAgentResponse;
import com.ironhack.smartqr.service.OpenAiAdminAgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent/openai")
@RequiredArgsConstructor
public class OpenAiAdminAgentController {

    private final OpenAiAdminAgentService openAiAdminAgentService;

    @PostMapping("/ask")
    @ResponseStatus(HttpStatus.OK)
    public OpenAiAgentResponse askOpenAiAgent(
            @Valid @RequestBody OpenAiAgentRequest request
    ) {
        return openAiAdminAgentService.askOpenAiAgent(request);
    }
}
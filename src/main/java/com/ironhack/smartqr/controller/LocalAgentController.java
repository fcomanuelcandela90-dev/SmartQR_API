package com.ironhack.smartqr.controller;

import com.ironhack.smartqr.dto.agent.LocalAgentRequest;
import com.ironhack.smartqr.dto.agent.LocalAgentResponse;
import com.ironhack.smartqr.service.LocalAgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent/local")
@RequiredArgsConstructor
public class LocalAgentController {

    private final LocalAgentService localAgentService;

    @PostMapping("/ask")
    @ResponseStatus(HttpStatus.OK)
    public LocalAgentResponse askLocalAgent(
            @Valid @RequestBody LocalAgentRequest request
    ) {
        return localAgentService.askLocalAgent(request);
    }

}

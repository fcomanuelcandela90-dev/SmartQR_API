package com.ironhack.smartqr.controller;

import com.ironhack.smartqr.dto.ai.ComboRecommendationRequest;
import com.ironhack.smartqr.dto.ai.ComboRecommendationResponse;
import com.ironhack.smartqr.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/combo-recommendation")
    @ResponseStatus(HttpStatus.OK)
    public ComboRecommendationResponse generateComboRecommendation(
            @Valid @RequestBody ComboRecommendationRequest request
    ) {
        return aiService.generateComboRecommendation(request);
    }

}

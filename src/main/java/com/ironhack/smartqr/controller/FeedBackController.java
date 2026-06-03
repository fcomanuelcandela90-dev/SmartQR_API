package com.ironhack.smartqr.controller;

import com.ironhack.smartqr.dto.feedback.FeedbackRequest;
import com.ironhack.smartqr.dto.feedback.FeedbackResponse;
import com.ironhack.smartqr.dto.feedback.FeedbackStatisticsResponse;
import com.ironhack.smartqr.service.FeedBackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedBackController {

    private final FeedBackService feedBackService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FeedbackResponse createFeedback(
            Authentication authentication,
            @Valid @RequestBody FeedbackRequest request) {
        return feedBackService.createFeedback(authentication.getName(), request);
    }

    @GetMapping("/statistics")
    @ResponseStatus(HttpStatus.OK)
    public FeedbackStatisticsResponse getFeedbackStatistics() {
        return feedBackService.getFeedbackStatistics();
    }
}
package com.ironhack.smartqr.service;

import com.ironhack.smartqr.dto.ai.ComboRecommendationRequest;
import com.ironhack.smartqr.dto.ai.ComboRecommendationResponse;
import com.ironhack.smartqr.entity.Product;
import com.ironhack.smartqr.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ProductRepository productRepository;
    private final ChatClient chatClient;

    public ComboRecommendationResponse generateComboRecommendation(ComboRecommendationRequest request) {
        List<Product> availableProducts = productRepository.findByAvailableTrue();

        if (availableProducts.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot generate combo recommendation: there are no available products in the menu."
            );
        }

        String availableMenu = buildAvailableMenuForPrompt(availableProducts);

        String customerRequest = """
            Customer preferences: %s
            Maximum total budget: %s EUR
            Number of people: %d

            Available products:
            %s
            """.formatted(
                request.preferences(),
                request.maxBudget(),
                request.numberOfPeople(),
                availableMenu
        );

        String recommendation = chatClient.prompt()
                .system("""
                    You are the SmartQR restaurant assistant.
                    Recommend a combo for the customer using only products included in the available menu.

                    Rules:
                    - Reply in Spanish.
                    - Respect the maximum total budget.
                    - Consider the number of people and customer preferences.
                    - Use exact product names from the available menu.
                    - Include quantities and prices.
                    - Include the estimated total price.
                    - Include a short reason for your recommendation.
                    - Do not invent products, prices or discounts.
                    - Do not create an order. This response is only a recommendation.
                    """)
                .user(customerRequest)
                .call()
                .content();

        if (recommendation == null || recommendation.isBlank()) {
            throw new IllegalStateException(
                    "OpenAI did not return a combo recommendation."
            );
        }

        return new ComboRecommendationResponse(
                request.preferences(),
                request.maxBudget(),
                request.numberOfPeople(),
                recommendation.trim()
        );
    }

    private String buildAvailableMenuForPrompt(List<Product> availableProducts) {
        StringBuilder menuBuilder = new StringBuilder();

        for (Product product : availableProducts) {
            menuBuilder.append("- ")
                    .append(product.getName())
                    .append(" | Category: ")
                    .append(product.getCategory())
                    .append(" | Price: ")
                    .append(product.getPrice())
                    .append(" EUR");

            if (product.getDescription() != null && !product.getDescription().isBlank()) {
                menuBuilder.append(" | Description: ")
                        .append(product.getDescription());
            }

            menuBuilder.append("\n");
        }

        return menuBuilder.toString();
    }

}

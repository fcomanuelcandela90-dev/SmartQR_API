package com.ironhack.smartqr.security;

import com.ironhack.smartqr.security.filters.CustomAuthenticationFilter;
import com.ironhack.smartqr.security.filters.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()

                        .requestMatchers(HttpMethod.GET, "/products/menu").permitAll()

                        .requestMatchers(HttpMethod.POST, "/qr/table/**").hasAnyRole("EMPLOYEE", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/products/out-of-stock").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/orders").hasRole("CUSTOMER")

                        .requestMatchers(HttpMethod.GET, "/orders/kitchen/queue").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/orders/table/**").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/orders/*/items").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/orders/*/status").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/orders/*/cancel").hasAnyRole("EMPLOYEE", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/orders/*").hasAnyRole("CUSTOMER", "EMPLOYEE", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/payments/card").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/payments/cash/request").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/payments/cash/*/confirm").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/payments/ticket/*").hasAnyRole("CUSTOMER", "EMPLOYEE", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/feedback").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/feedback/statistics").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/ai/combo-recommendation").hasRole("CUSTOMER")

                        .requestMatchers(HttpMethod.GET, "/dashboard/metrics").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/dashboard/charts/income-by-payment-method").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/dashboard/charts/product-sales").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/agent/local/ask").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/agent/openai/ask").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/mcp").hasRole("ADMIN")

                        .anyRequest().denyAll()
                )
                .addFilter(
                        new CustomAuthenticationFilter(
                                authenticationManager(authenticationConfiguration),
                                jwtSecret,
                                jwtExpiration
                        )
                )
                .addFilterBefore(
                        new CustomAuthorizationFilter(jwtSecret),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}

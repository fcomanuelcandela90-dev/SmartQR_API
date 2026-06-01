package com.ironhack.smartqr.security;

import com.ironhack.smartqr.security.filters.CustomAuthenticationFilter;
import com.ironhack.smartqr.security.filters.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Public authentication endpoints
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()

                        // Public digital menu: customers reach it by scanning a physical table QR
                        .requestMatchers(HttpMethod.GET, "/products/menu").permitAll()

                        // QR generation is an internal staff operation
                        .requestMatchers(HttpMethod.POST, "/qr/table/**").hasAnyRole("EMPLOYEE", "ADMIN")

                        // Product catalogue management
                        .requestMatchers(HttpMethod.GET, "/products/out-of-stock").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")

                        // Customer order creation
                        .requestMatchers(HttpMethod.POST, "/orders").hasRole("CUSTOMER")

                        // Internal order management
                        .requestMatchers(HttpMethod.GET, "/orders/kitchen/queue").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/orders/table/**").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/orders/*/items").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/orders/*/status").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/orders/*/cancel").hasAnyRole("EMPLOYEE", "ADMIN")

                        // Order detail access: ownership validation can be reinforced later
                        .requestMatchers(HttpMethod.GET, "/orders/*").hasAnyRole("CUSTOMER", "EMPLOYEE", "ADMIN")

                        // Customer payment actions
                        .requestMatchers(HttpMethod.POST, "/payments/card").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/payments/cash/request").hasRole("CUSTOMER")

                        // Cash confirmation belongs to cashier or manager
                        .requestMatchers(HttpMethod.PUT, "/payments/cash/*/confirm").hasAnyRole("EMPLOYEE", "ADMIN")

                        // Printable ticket consultation
                        .requestMatchers(HttpMethod.GET, "/payments/ticket/*").hasAnyRole("CUSTOMER", "EMPLOYEE", "ADMIN")

                        // Any new endpoint must be explicitly classified before being exposed
                        .anyRequest().denyAll()
                )
                .addFilter(new CustomAuthenticationFilter(authenticationManager(authenticationConfiguration)))
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


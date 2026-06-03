package com.ironhack.smartqr.service;

import com.ironhack.smartqr.dto.auth.AuthResponse;
import com.ironhack.smartqr.dto.auth.RegisterRequest;
import com.ironhack.smartqr.entity.User;
import com.ironhack.smartqr.exception.ConflictException;
import com.ironhack.smartqr.enums.UserRole;
import com.ironhack.smartqr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ConflictException(
                    "Cannot register user: this email is already in use."
            );
        }
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.CUSTOMER);

        userRepository.save(user);

        return new AuthResponse("User registered successfully");
    }
}
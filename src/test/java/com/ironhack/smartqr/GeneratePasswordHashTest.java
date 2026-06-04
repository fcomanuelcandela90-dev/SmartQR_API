package com.ironhack.smartqr;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

class GeneratePasswordHashTest {

    @Test
    void generateDemoUserPasswordHashes() { // Utilidad solo para test, generador de hash para usuarios locales de demostración.
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        System.out.println("ADMIN_HASH = " + encoder.encode("Admin123!"));
        System.out.println("EMPLOYEE_HASH = " + encoder.encode("Employee123!"));
    }

}
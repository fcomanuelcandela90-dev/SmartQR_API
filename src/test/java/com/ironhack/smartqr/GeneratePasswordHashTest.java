package com.ironhack.smartqr;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class GeneratePasswordHashTest {

    @Test
    void generateDemoUserPasswordHashes() { // Utilidad solo para test, generador de hash para usuarios locales de demostración.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("ADMIN_HASH = " + encoder.encode("Admin123!"));
        System.out.println("EMPLOYEE_HASH = " + encoder.encode("Employee123!"));
    }

}
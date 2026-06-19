package com.ironhack.smartqr.dev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Component
@Profile("dev")
@ConditionalOnProperty(prefix = "smartqr.dev", name = "print-hashes", havingValue = "true")
public class PasswordHashPrinter implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PasswordHashPrinter.class);

    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    public PasswordHashPrinter(PasswordEncoder passwordEncoder, Environment environment) {
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        String value = environment.getProperty("smartqr.dev.passwords", "");

        if (!StringUtils.hasText(value)) {
            log.info("Provide passwords to hash with '--smartqr.dev.passwords=password1,password2' when running the application.");
            return;
        }

        Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .forEach(password -> log.info("bcrypt hash for [{}]: {}", password, passwordEncoder.encode(password)));
    }
}

package com.ironhack.smartqr.config;

import com.ironhack.smartqr.entity.User;
import com.ironhack.smartqr.enums.UserRole;
import com.ironhack.smartqr.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DataSeedRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeedRunner.class);

    private static final String ADMIN_EMAIL = "admin@smartqr.com";
    private static final String EMPLOYEE_EMAIL = "employee@smartqr.com";
    private static final String CUSTOMER_EMAIL = "customer@smartqr.com";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    public DataSeedRunner(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          Environment environment) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.debug("Skipping initial user seed because users table is not empty");
            return;
        }

        boolean isDevProfile = environment.acceptsProfiles(Profiles.of("dev"));

        String adminPassword = resolveRequiredPassword("ADMIN_SEED_PASSWORD", "Admin123!", isDevProfile, "admin");
        String employeePassword = resolveRequiredPassword("EMPLOYEE_SEED_PASSWORD", "Employee123!", isDevProfile, "employee");
        String customerPassword = resolveOptionalPassword("CUSTOMER_SEED_PASSWORD", "Customer123!", isDevProfile, "customer");

        seedUser("SmartQR Admin", ADMIN_EMAIL, adminPassword, UserRole.ADMIN);
        seedUser("SmartQR Employee", EMPLOYEE_EMAIL, employeePassword, UserRole.EMPLOYEE);

        if (customerPassword != null) {
            seedUser("SmartQR Customer", CUSTOMER_EMAIL, customerPassword, UserRole.CUSTOMER);
        }

        if (customerPassword != null) {
            log.info("Seeded SmartQR demo users: ADMIN, EMPLOYEE and CUSTOMER");
        } else {
            log.info("Seeded SmartQR demo users: ADMIN and EMPLOYEE");
        }
    }

    private String resolveRequiredPassword(String envKey, String devFallback, boolean isDevProfile, String userDescription) {
        String password = environment.getProperty(envKey);
        if (StringUtils.hasText(password)) {
            return password;
        }

        if (isDevProfile && StringUtils.hasText(devFallback)) {
            log.warn("{} environment variable not set. Using development fallback password for {} user.", envKey, userDescription);
            return devFallback;
        }

        throw new IllegalStateException("Environment variable " + envKey + " must be defined to seed initial users safely.");
    }

    private String resolveOptionalPassword(String envKey, String devFallback, boolean isDevProfile, String userDescription) {
        String password = environment.getProperty(envKey);
        if (StringUtils.hasText(password)) {
            return password;
        }

        if (isDevProfile && StringUtils.hasText(devFallback)) {
            log.warn("{} environment variable not set. Using development fallback password for {} user.", envKey, userDescription);
            return devFallback;
        }

        return null;
    }

    private void seedUser(String name, String email, String rawPassword, UserRole role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        userRepository.save(user);
    }
}

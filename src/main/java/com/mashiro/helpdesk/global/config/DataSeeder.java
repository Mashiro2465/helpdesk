package com.mashiro.helpdesk.global.config;

import com.mashiro.helpdesk.domain.user.AppUser;
import com.mashiro.helpdesk.domain.user.UserRole;
import com.mashiro.helpdesk.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                userRepository.save(AppUser.create(
                        "admin",
                        encoder.encode("admin1234"),
                        UserRole.ROLE_ADMIN
                ));
            }

            if (!userRepository.existsByUsername("user")) {
                userRepository.save(AppUser.create(
                        "user",
                        encoder.encode("user1234"),
                        UserRole.ROLE_USER
                ));
            }
        };
    }
}

package com.mashiro.helpdesk.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // swagger는 열어두기
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // 관리자 전용
                        .requestMatchers("/api/tickets/search").hasRole("ADMIN")
                        .requestMatchers("/api/tickets/*/status").hasRole("ADMIN")

                        // 나머지 API는 일단 인증만
                        .requestMatchers("/api/**").authenticated()

                        // 그 외는 허용
                        .anyRequest().permitAll()
                )
                // JWT 전까지는 Basic 인증으로 간단히 테스트
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    UserDetailsService users() {
        UserDetails user = User.withUsername("user")
                .password("{noop}user1234")
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password("{noop}admin1234")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}

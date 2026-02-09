package com.mashiro.helpdesk.global.config;

import com.mashiro.helpdesk.global.security.JwtAuthFilter;
import com.mashiro.helpdesk.global.security.JwtTokenProvider;
import com.mashiro.helpdesk.global.security.RestAccessDeniedHandler;
import com.mashiro.helpdesk.global.security.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider jwt) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // 관리자 전용
                        .requestMatchers("/api/tickets/search").hasRole("ADMIN")
                        .requestMatchers("/api/tickets/*/status").hasRole("ADMIN")

                        // 나머지 API는 인증 필요
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                        .accessDeniedHandler(new RestAccessDeniedHandler())
                )
                .addFilterBefore(new JwtAuthFilter(jwt), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

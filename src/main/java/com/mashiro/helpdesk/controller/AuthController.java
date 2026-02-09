package com.mashiro.helpdesk.controller;

import com.mashiro.helpdesk.domain.user.AppUser;
import com.mashiro.helpdesk.global.security.JwtTokenProvider;
import com.mashiro.helpdesk.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenProvider jwt;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public AuthController(JwtTokenProvider jwt, UserRepository userRepository, PasswordEncoder encoder) {
        this.jwt = jwt;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest req) {
        AppUser user = userRepository.findByUsername(req.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials"));

        if (!encoder.matches(req.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials");
        }

        String token = jwt.createToken(user.getUsername(), user.getRole().name());
        return Map.of("token", token);
    }
}

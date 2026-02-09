package com.mashiro.helpdesk.controller;

import com.mashiro.helpdesk.global.security.JwtTokenProvider;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenProvider jwt;

    public AuthController(JwtTokenProvider jwt) {
        this.jwt = jwt;
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest req) {
        // 임시: 하드코딩 로그인 (다음에 DB 유저로 교체)
        if ("user".equals(req.username()) && "user1234".equals(req.password())) {
            String token = jwt.createToken("user", "ROLE_USER");
            return Map.of("token", token);
        }
        if ("admin".equals(req.username()) && "admin1234".equals(req.password())) {
            String token = jwt.createToken("admin", "ROLE_ADMIN");
            return Map.of("token", token);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials");

    }
}

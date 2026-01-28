package org.example.google_lms.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.google_lms.domain.dto.request.RegisterRequest;
import org.example.google_lms.domain.dto.response.SuccessResponse;
import org.example.google_lms.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/student")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.registerStudent(request);
        return ResponseEntity.ok(SuccessResponse.ok("User registered successfully"));
    }


}

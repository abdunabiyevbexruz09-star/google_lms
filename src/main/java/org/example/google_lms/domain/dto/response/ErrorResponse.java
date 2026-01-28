package org.example.google_lms.domain.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String message,
        Map<String, String> errors
) {
    public static ResponseEntity<ErrorResponse> of(HttpStatus status, String message, Map<String, String> errors) {
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(status.value())
                        .message(message)
                        .build());

    }

    public static ResponseEntity<ErrorResponse> of(HttpStatus status, String message) {
        return of(status, message, null);
    }
}
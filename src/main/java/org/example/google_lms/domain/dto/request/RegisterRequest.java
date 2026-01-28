package org.example.google_lms.domain.dto.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record RegisterRequest(
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("email")
        String email,
        @JsonProperty("password")
        String password
) {
}
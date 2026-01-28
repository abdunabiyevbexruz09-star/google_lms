package org.example.google_lms.domain.dto.response;
import lombok.Builder;

@Builder
public record SuccessResponse(
        String message,
        boolean status,
        int code
) {

    public  static SuccessResponse ok(String message){
        return SuccessResponse.builder()
                .message(message)
                .status(true)
                .code(200)
                .build();
    }

}
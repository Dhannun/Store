package com.touchit.foodlify.dto.response;

import lombok.Builder;
import lombok.Data;

import static org.springframework.http.HttpStatus.OK;

@Data
@Builder
public class MessageResponse {

    public static Response successMessage(String message) {
        return Response.builder()
                .code(OK.value())
                .status(OK.getReasonPhrase())
                .message(message)
                .build();
    }
}

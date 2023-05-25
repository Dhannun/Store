package com.touchit.foodlify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.springframework.http.HttpStatus.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
  private int code;
  private String status;
  private String message;
  private Object body;

  public static Response OK(Object body) {
    return Response.builder()
            .code(OK.value())
            .status(OK.getReasonPhrase())
            .body(body)
            .message("Successful")
            .build();
  }

  public static Response Created() {
    return Response.builder()
            .code(CREATED.value())
            .status(CREATED.getReasonPhrase())
            .message("Created Successfully")
            .build();
  }

  public static Response BadRequest(String message) {
    return Response.builder()
            .code(BAD_REQUEST.value())
            .status(BAD_REQUEST.getReasonPhrase())
            .message(message)
            .build();
  }

  public static Response NotFound(String resource) {
    return Response.builder()
            .code(NOT_FOUND.value())
            .status(NOT_FOUND.getReasonPhrase())
            .message( resource + " Not Found")
            .build();
  }

  public static Response Forbidden(String message) {
    return Response.builder()
            .code(FORBIDDEN.value())
            .status(FORBIDDEN.getReasonPhrase())
            .message(message)
            .build();
  }

}

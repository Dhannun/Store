package com.touchit.foodlify.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder
public class LoginRequest {
  @NonNull
  private String username;
  @NonNull
  private String password;
}

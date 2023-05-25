package com.touchit.foodlify.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder
public class PhoneNumberRegistrationRequest {
  @NonNull
  private String phone_number;
  @NonNull
  private String password;
}

package com.touchit.foodlify.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder
public class PhoneNumberProfileUpdateRequest {
  @NonNull
  private String email;
  @NonNull
  private String full_name;
}

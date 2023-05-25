package com.touchit.foodlify.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantRegistrationRequest {
  private String email;
  private String password;
}

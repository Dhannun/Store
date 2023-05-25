package com.touchit.foodlify.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantKYCRequest {
  private RestaurantKYC restaurant_kyc;
  private RestaurantPerson restaurant_personal;
}

package com.touchit.foodlify.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantKYC {
  private String name;
  private String address;
  private String website;
  private Boolean registered;
  private Integer branch;
  private Long range_fund;
}

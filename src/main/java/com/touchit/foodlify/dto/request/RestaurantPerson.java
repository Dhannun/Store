package com.touchit.foodlify.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class RestaurantPerson {
  private String first_name;
  private String surname;
  private String email;
  private String phone_number;
  private String identity;
  private MultipartFile identityFile;
//  private String identityFile;
}

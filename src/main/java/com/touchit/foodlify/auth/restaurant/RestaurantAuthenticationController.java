package com.touchit.foodlify.auth.restaurant;

import com.touchit.foodlify.dto.request.LoginRequest;
import com.touchit.foodlify.dto.request.RestaurantKYC;
import com.touchit.foodlify.dto.request.RestaurantKYCRequest;
import com.touchit.foodlify.dto.request.RestaurantRegistrationRequest;
import com.touchit.foodlify.univrsal.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/restaurant")
@AllArgsConstructor
public class RestaurantAuthenticationController {

  private final RestaurantAuthenticationService restaurantAuthenticationService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse> register(
      @RequestBody RestaurantRegistrationRequest registrationRequest
  ){
    return restaurantAuthenticationService.register(registrationRequest);
  }

  @GetMapping("/confirm_otp")
  public ResponseEntity<ApiResponse> conformOtp(
      @RequestParam("token") String token
  ) {
    return restaurantAuthenticationService.confirmToken(token);
  }

  @PutMapping("kyc/{email}")
  public ResponseEntity<ApiResponse> restaurantKyc(
      @PathVariable String email,
      @RequestBody RestaurantKYC kycRequest
      ) {
      return restaurantAuthenticationService.setRestaurantKYC(email, kycRequest);
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse> login(
      @RequestBody LoginRequest loginRequest
  ){
    return restaurantAuthenticationService.login(loginRequest);
  }

}

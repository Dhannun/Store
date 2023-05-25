package com.touchit.foodlify.auth.appuser;

import com.touchit.foodlify.dto.request.*;
import com.touchit.foodlify.univrsal.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth/user")
public class AppUserAuthenticationController {

  private final AppUserAuthenticationService authenticationService;

  @PostMapping("/number/registration")
  public ResponseEntity<ApiResponse> smsRegister(
      @Validated @RequestBody PhoneNumberRegistrationRequest registrationRequest
  ){
    return authenticationService.register(registrationRequest);
  }

  @PostMapping("/email/registration")
  public ResponseEntity<ApiResponse> emilRegister(
      @Validated @RequestBody EmailRegistrationRequest registrationRequest
  ){
    return authenticationService.register(registrationRequest);
  }

  @GetMapping("/sms/resend_otp")
  public ResponseEntity<ApiResponse> resendSmsOtp(
      @RequestParam("phone_number") String phoneNumber
  ) {
    return authenticationService.resendSmsOtp(phoneNumber);
  }

  @GetMapping("/email/resend_otp")
  public ResponseEntity<ApiResponse> resendEmailOtp(
      @RequestParam("email") String email
  ) {
    return authenticationService.resendEmailOtp(email);
  }

  @GetMapping("/confirm_otp")
  public ResponseEntity<ApiResponse> conformOtp(
      @RequestParam("token") String token
  ) {
   return authenticationService.confirmToken(token);
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse> login(
      @RequestBody LoginRequest loginRequest
  ){
    return authenticationService.login(loginRequest);
  }

  @PutMapping("/update_profile/email/{email}")
  public ResponseEntity<ApiResponse> updateProfileByEmail(
      @PathVariable String email,
      @RequestBody EmailProfileUpdateRequest profileUpdateRequest
  ){
    return authenticationService.updateProfileByEmail(email, profileUpdateRequest);
  }

  @PutMapping("/update_profile/phone/{phone}")
  public ResponseEntity<ApiResponse> updateProfileByPhone(
      @PathVariable String phone,
      @RequestBody PhoneNumberProfileUpdateRequest profileUpdateRequest
  ){
    return authenticationService.updateProfileByPhoneNumber(phone, profileUpdateRequest);
  }

  @PostMapping("/refresh_token")
  public ResponseEntity<ApiResponse> refresh(
      HttpServletRequest request
  ) {
    return authenticationService.refreshToken(request);
  }

}

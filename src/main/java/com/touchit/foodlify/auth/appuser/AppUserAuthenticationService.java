package com.touchit.foodlify.auth.appuser;

import com.touchit.foodlify.appuser.AppUser;
import com.touchit.foodlify.appuser.AppUserService;
import com.touchit.foodlify.config.JwtService;
import com.touchit.foodlify.dto.request.*;
import com.touchit.foodlify.dto.response.login.Login;
import com.touchit.foodlify.exceptions.InvalidResourceException;
import com.touchit.foodlify.exceptions.OperationFailedException;
import com.touchit.foodlify.otp.appuser.AppUserConfirmationToken;
import com.touchit.foodlify.otp.appuser.AppUserConfirmationTokenService;
import com.touchit.foodlify.jwttoken.appuser.AppUserToken;
import com.touchit.foodlify.jwttoken.appuser.AppUserTokenService;
import com.touchit.foodlify.univrsal.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.touchit.foodlify.jwttoken.TokenType.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class AppUserAuthenticationService {

  private final AppUserService appUserService;
  private final AppUserConfirmationTokenService appUserConfirmationTokenService;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final AppUserTokenService appUserTokenService;

  public ResponseEntity<ApiResponse> register(
      PhoneNumberRegistrationRequest registrationRequest
  ) {
    boolean registered = appUserService.register(registrationRequest);

    if (registered)
      return new ResponseEntity<>(new ApiResponse("Registration successful"), CREATED);

    throw new InvalidResourceException("User Already Registered");
  }

  public ResponseEntity<ApiResponse> register(
      EmailRegistrationRequest registrationRequest
  ) {
    boolean registered = appUserService.register(registrationRequest);

    if (registered)
      return new ResponseEntity<>(new ApiResponse("Registration successful"), CREATED);

    throw new OperationFailedException("Registration failed, please try again");
  }

  public ResponseEntity<ApiResponse> login(
      LoginRequest loginRequest
  ) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername() + "_USER",
            loginRequest.getPassword()
        )
    );

    var appUser = appUserService.findAppUserByUsername(loginRequest.getUsername());
    var jwtToken = jwtService.generateToken(appUser);
    var refreshToken = jwtService.generateRefreshToken(appUser);

    revokeAllUserTokens(appUser);

    saveUserToken(appUser, jwtToken);

    return new ResponseEntity<>(
        new ApiResponse(
            "User logged in successfully",
            Login.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build()),
        OK);

  }

  public ResponseEntity<ApiResponse> refreshToken(
      HttpServletRequest request
  ) {
    final String authHeader = request.getHeader(AUTHORIZATION);
    final String refreshToken;
    final String username;

    if (authHeader == null || !authHeader.startsWith("Bearer ")){
      throw new InvalidResourceException("No or Invalid refresh token");
    }

    refreshToken = authHeader.substring(7);
    username = jwtService.extractUsername(refreshToken); // todo: extract user email from refresh token

    if (username != null) {

      var appUser = this.appUserService.findAppUserByUsername(username);

      if (jwtService.isTokenValid(refreshToken, appUser)) {
        var newAccessToken = jwtService.generateToken(appUser);

        revokeAllUserTokens(appUser);

        saveUserToken(appUser, newAccessToken);

        return new ResponseEntity<>(
            new ApiResponse(
                "User logged in successfully",
                Login.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .build()),
            OK);
      }else
        throw new InvalidResourceException("Invalid Refresh Token");
    }else
      throw new InvalidResourceException("Invalid Refresh Token");
  }

  public ResponseEntity<ApiResponse> confirmToken(
      String token
  ) {
    AppUserConfirmationToken appUserConfirmationToken = appUserConfirmationTokenService.getToken(token);

    if (appUserConfirmationToken.getConfirmedAt() != null) {
      throw new InvalidResourceException("Token Already Confirmed");
    }

    LocalDateTime expiredAt = appUserConfirmationToken.getExpiresAt();

    if (expiredAt.isBefore(LocalDateTime.now())) {
      throw new InvalidResourceException("Token Expired");
    }

    appUserConfirmationTokenService.confirmToken(token);
    appUserService.enableUserById(appUserConfirmationToken.getAppUser().getId());
    appUserConfirmationToken.setToken("XXXX");


    return new ResponseEntity<>(new ApiResponse("OTP Confirmed"), OK);
  }

  public ResponseEntity<ApiResponse> resendSmsOtp(String phoneNumber) {
    boolean resent = appUserService.resendSmsOtp(phoneNumber);
    if (resent)
      return new ResponseEntity<>(new ApiResponse("OTP Resent successfully"), OK);

    throw new OperationFailedException("Sorry, something went wrong, please try again");
  }

  public ResponseEntity<ApiResponse> resendEmailOtp(String email) {
    boolean resent = appUserService.resendEmailOtp(email);

    if (resent)
      return new ResponseEntity<>(new ApiResponse("OTP Resent successfully"), OK);

    throw new OperationFailedException("Sorry, something went wrong, please try again");
  }

  private void revokeAllUserTokens(AppUser appUser) {

    var validTokens = appUserTokenService.findAllValidTokenByUser(appUser.getId());

    if (validTokens.isEmpty()) {
      return;
    }

    validTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });

    appUserTokenService.saveAllTokens(validTokens);
  }

  private void saveUserToken(AppUser appUser, String jwtToken) {
    var token = AppUserToken.builder()
        .appUser(appUser)
        .token(jwtToken)
        .tokenType(BEARER)
        .expired(false)
        .revoked(false)
        .build();
    appUserTokenService.saveToken(token);
  }

  public ResponseEntity<ApiResponse> updateProfileByEmail(String email, EmailProfileUpdateRequest profileUpdateRequest) {
    var appUserByEmail = appUserService.findAppUserByEmail(email);
    appUserByEmail.setPhoneNumber(profileUpdateRequest.getPhone_number());
    appUserByEmail.setFullName(profileUpdateRequest.getFull_name());
    return new ResponseEntity<>(new ApiResponse("Profile Updated Successfully"), OK);
  }

  public ResponseEntity<ApiResponse> updateProfileByPhoneNumber(String phone, PhoneNumberProfileUpdateRequest profileUpdateRequest) {
    var appUserByEmail = appUserService.findAppUserByPhoneNumber(phone);
    appUserByEmail.setEmail(profileUpdateRequest.getEmail());
    appUserByEmail.setFullName(profileUpdateRequest.getFull_name());
    return new ResponseEntity<>(new ApiResponse("Profile Updated Successfully"), OK);
  }

}

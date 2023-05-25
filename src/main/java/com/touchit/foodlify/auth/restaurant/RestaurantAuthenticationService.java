package com.touchit.foodlify.auth.restaurant;

import com.touchit.foodlify.config.JwtService;
import com.touchit.foodlify.dto.request.LoginRequest;
import com.touchit.foodlify.dto.request.RestaurantKYC;
import com.touchit.foodlify.dto.request.RestaurantRegistrationRequest;
import com.touchit.foodlify.dto.response.login.Login;
import com.touchit.foodlify.exceptions.InvalidResourceException;
import com.touchit.foodlify.exceptions.OperationFailedException;
import com.touchit.foodlify.jwttoken.TokenType;
import com.touchit.foodlify.jwttoken.restaurant.RestaurantToken;
import com.touchit.foodlify.jwttoken.restaurant.RestaurantTokenService;
import com.touchit.foodlify.otp.restaurant.RestaurantConfirmationToken;
import com.touchit.foodlify.otp.restaurant.RestaurantConfirmationTokenService;
import com.touchit.foodlify.restaurant.Restaurant;
import com.touchit.foodlify.restaurant.RestaurantService;
import com.touchit.foodlify.univrsal.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.touchit.foodlify.jwttoken.TokenType.BEARER;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
public class RestaurantAuthenticationService {

  private final RestaurantService restaurantService;
  private final RestaurantConfirmationTokenService restaurantConfirmationTokenService;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final RestaurantTokenService restaurantTokenService;


  public ResponseEntity<ApiResponse> register(RestaurantRegistrationRequest registrationRequest) {
    boolean registered = restaurantService.register(registrationRequest);

    if (registered)
      return new ResponseEntity<>(new ApiResponse("Registration successful"), CREATED);

    throw new OperationFailedException("Registration failed, please try again");
  }

  public ResponseEntity<ApiResponse> confirmToken(String token) {
    RestaurantConfirmationToken restaurantConfirmationToken = restaurantConfirmationTokenService.getToken(token);

    if (restaurantConfirmationToken.getConfirmedAt() != null) {
      throw new InvalidResourceException("Token Already Confirmed");
    }

    LocalDateTime expiredAt = restaurantConfirmationToken.getExpiresAt();

    if (expiredAt.isBefore(LocalDateTime.now())) {
      throw new InvalidResourceException("Token Expired");
    }

    restaurantConfirmationTokenService.confirmToken(token);
    restaurantService.enableRestaurantById(restaurantConfirmationToken.getRestaurant().getId());
    restaurantConfirmationToken.setToken("XXXX");


    return new ResponseEntity<>(new ApiResponse("OTP Confirmed"), OK);
  }

  public ResponseEntity<ApiResponse> setRestaurantKYC(String email, RestaurantKYC kycRequest) {
    boolean doneKYC = restaurantService.setRestaurantKYC(email, kycRequest);

    if (doneKYC)
      return new ResponseEntity<>(new ApiResponse("KYC done successfully"), OK);

    throw new OperationFailedException("KYC failed, please try again later");
  }

  public ResponseEntity<ApiResponse> login(LoginRequest loginRequest) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername() + "_RESTAURANT",
            loginRequest.getPassword()
        )
    );

    Restaurant restaurant = restaurantService.findAppUserByEmail(loginRequest.getUsername());
    var jwtToken = jwtService.generateToken(restaurant);
    var refreshToken = jwtService.generateRefreshToken(restaurant);

    revokeAllUserTokens(restaurant);

    saveUserToken(restaurant, jwtToken);

    return new ResponseEntity<>(
        new ApiResponse(
            "User logged in successfully",
            Login.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build()),
        OK);

  }

  private void revokeAllUserTokens(Restaurant restaurant) {

    var validTokens = restaurantTokenService.findAllValidTokenByRestaurantId(restaurant.getId());

    if (validTokens.isEmpty()) {
      return;
    }

    validTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });

    restaurantTokenService.saveAllTokens(validTokens);
  }

  private void saveUserToken(Restaurant restaurant, String jwtToken) {

    var token = RestaurantToken.builder()
        .restaurant(restaurant)
        .token(jwtToken)
        .tokenType(BEARER)
        .expired(false)
        .revoked(false)
        .build();
    restaurantTokenService.saveToken(token);
  }
}

package com.touchit.foodlify.otp.restaurant;

import com.touchit.foodlify.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class RestaurantConfirmationTokenService {

  private final RestaurantConfirmationTokenRepository appUserConfirmationTokenRepository;

  public void saveConfirmationToken(RestaurantConfirmationToken token) {
    appUserConfirmationTokenRepository.save(token);
  }

  public RestaurantConfirmationToken getToken(String token){
    return appUserConfirmationTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Token not found"));
  }

  public void confirmToken(String confirmationToken) {
    appUserConfirmationTokenRepository.updateConfirmedAt(confirmationToken, LocalDateTime.now());
  }

  public String generateToken() {
    return Integer.toString(10000 + new Random().nextInt(9999)).substring(0,4);
  }

  public RestaurantConfirmationToken findTokenByRestaurantId(Long id) {
    return appUserConfirmationTokenRepository.findRestaurantId(id).orElseThrow(() -> new ResourceNotFoundException("Token not found"));
  }
}

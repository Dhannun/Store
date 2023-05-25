package com.touchit.foodlify.otp.appuser;

import com.touchit.foodlify.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class AppUserConfirmationTokenService {

  private final AppUserConfirmationTokenRepository appUserConfirmationTokenRepository;

  public void saveConfirmationToken(AppUserConfirmationToken token) {
    appUserConfirmationTokenRepository.save(token);
  }

  public AppUserConfirmationToken getToken(String token){
    return appUserConfirmationTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Token not found"));
  }

  public void confirmToken(String confirmationToken) {
    appUserConfirmationTokenRepository.updateConfirmedAt(confirmationToken, LocalDateTime.now());
  }

  public String generateToken() {
    return Integer.toString(10000 + new Random().nextInt(9999)).substring(0,4);
  }

  public AppUserConfirmationToken findTokenByUserId(Long id) {
    return appUserConfirmationTokenRepository.findByUserId(id).orElseThrow(() -> new ResourceNotFoundException("Token not found"));
  }
}

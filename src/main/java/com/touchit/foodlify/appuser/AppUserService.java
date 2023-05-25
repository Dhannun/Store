package com.touchit.foodlify.appuser;

import com.touchit.foodlify.dto.request.EmailRegistrationRequest;
import com.touchit.foodlify.dto.request.PhoneNumberRegistrationRequest;
import com.touchit.foodlify.exceptions.ResourceNotFoundException;
import com.touchit.foodlify.otp.appuser.AppUserConfirmationToken;
import com.touchit.foodlify.otp.appuser.AppUserConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.touchit.foodlify.univrsal.Role.USER;

@Service
@AllArgsConstructor
@Log4j2
@Transactional
public class AppUserService {

  private final AppUserRepository appUserRepository;
  private final AppUserConfirmationTokenService appUserConfirmationTokenService;
  private final PasswordEncoder passwordEncoder;

  public boolean register(PhoneNumberRegistrationRequest registrationRequest) {
    boolean userExists = appUserRepository.findByPhoneNumber(registrationRequest.getPhone_number()).isPresent();

    if (userExists){
      return false;
    }else {

//      var appUser = AppUser.builder()
//          .phoneNumber(registrationRequest.getPhoneNumber())
//          .password(passwordEncoder.encode(registrationRequest.getPassword()))
//          .role(USER)
//          .build();

      AppUser appUser = new AppUser(
          null,
          null,
          registrationRequest.getPhone_number(),
          passwordEncoder.encode(registrationRequest.getPassword()),
          USER,
          false,
          false
      );

      //TODO: sent OTP (SMS)
//      String token = confirmationTokenService.generateToken();
      final String token = "2802";

      AppUserConfirmationToken appUserConfirmationToken = new AppUserConfirmationToken(
          token,
          LocalDateTime.now(),
          LocalDateTime.now().plusMinutes(30),
          appUser
      );

      return registerNewUser(appUser, appUserConfirmationToken);
    }
  }

  public boolean register(EmailRegistrationRequest registrationRequest) {
    boolean userExists = appUserRepository.findByEmail(registrationRequest.getEmail()).isPresent();

    if (userExists){
      return false;
    }else {

//      var appUser = AppUser.builder()
//          .email(registrationRequest.getEmail())
//          .password(passwordEncoder.encode(registrationRequest.getPassword()))
//          .role(USER)
//          .build();
      AppUser appUser = new AppUser(
          null,
          registrationRequest.getEmail(),
          null,
          passwordEncoder.encode(registrationRequest.getPassword()),
          USER,
          false,
          false
          );

      //TODO: sent OTP (SMS)
//      String token = confirmationTokenService.generateToken();
      final String token = "2802";

      AppUserConfirmationToken appUserConfirmationToken = new AppUserConfirmationToken(
          token,
          LocalDateTime.now(),
          LocalDateTime.now().plusMinutes(30),
          appUser
      );

      return registerNewUser(appUser, appUserConfirmationToken);
    }
  }

  public void enableUserById(Long id) {
    appUserRepository.enableAppUserById(id);
  }

/*  public void enableUserByEmail(String email) {
    appUserRepository.enableAppUserByEmail(email);
  }
  public void enableUserByPhoneNumber(String phoneNumber) {
    appUserRepository.enableAppUserByPhoneNumber(phoneNumber);
  }*/

  public AppUser findAppUserByEmail(String email) {
    return appUserRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  public AppUser findAppUserByPhoneNumber(String phoneNumber) {
    return appUserRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  private boolean registerNewUser(AppUser appuser, AppUserConfirmationToken token) {
    appUserRepository.save(appuser);
    appUserConfirmationTokenService.saveConfirmationToken(token);
    return true;
  }

  public boolean resendSmsOtp(String phoneNumber) {
    var appUser = findAppUserByPhoneNumber(phoneNumber);
    var confirmationToken = appUserConfirmationTokenService.findTokenByUserId(appUser.getId());

//    String newToken = confirmationTokenService.generateToken();
    String newToken = "2802";
    //TODO: Sent OTP (sms)
    confirmationToken.setToken(newToken);
    confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
    return true;
  }

  public boolean resendEmailOtp(String email) {
    var appUser = findAppUserByEmail(email);
    var confirmationToken = appUserConfirmationTokenService.findTokenByUserId(appUser.getId());

//    String newToken = confirmationTokenService.generateToken();
    String newToken = "2802";
    //TODO: Sent OTP (sms)
    confirmationToken.setToken(newToken);
    confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
    return true;
  }

  public AppUser findAppUserByUsername(String username) {
    return appUserRepository.findUserByLoginUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }
}

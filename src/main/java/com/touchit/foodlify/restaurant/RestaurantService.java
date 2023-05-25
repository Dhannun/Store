package com.touchit.foodlify.restaurant;

import com.touchit.foodlify.dto.request.RestaurantKYC;
import com.touchit.foodlify.dto.request.RestaurantRegistrationRequest;
import com.touchit.foodlify.exceptions.ResourceNotFoundException;
import com.touchit.foodlify.otp.restaurant.RestaurantConfirmationToken;
import com.touchit.foodlify.otp.restaurant.RestaurantConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.touchit.foodlify.univrsal.Role.RESTAURANT;

@Service
@RequiredArgsConstructor
public class RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final RestaurantConfirmationTokenService restaurantConfirmationTokenService;
  private final PasswordEncoder passwordEncoder;

  public boolean register(RestaurantRegistrationRequest registrationRequest) {
    boolean restaurantExists = restaurantRepository.findByEmail(registrationRequest.getEmail()).isPresent();

    if (restaurantExists){
      return false;
    }else {
      var restaurant = new Restaurant(
          null,
          null,
          registrationRequest.getEmail(),
          passwordEncoder.encode(registrationRequest.getPassword()),
          null,
          false,
          0,
          0L,
          UUID.randomUUID().toString().replace("-", ""),
          false,
          false,
          RESTAURANT
      );


//          Restaurant.builder()
//          .email(registrationRequest.getEmail())
//          .password(passwordEncoder.encode(registrationRequest.getPassword()))
//          .restaurantId(UUID.randomUUID().toString().replace("-", ""))
//          .locked(false)
//          .enabled(false)
//          .build();

      //TODO: sent OTP (SMS)
//      String token = confirmationTokenService.generateToken();
      final String token = "2802";

      RestaurantConfirmationToken restaurantConfirmationToken = new RestaurantConfirmationToken(
          token,
          LocalDateTime.now(),
          LocalDateTime.now().plusMinutes(30),
          restaurant
      );

      return registerNewRestaurant(restaurant, restaurantConfirmationToken);

    }
  }

  private boolean registerNewRestaurant(Restaurant restaurant, RestaurantConfirmationToken restaurantConfirmationToken) {
    restaurantRepository.save(restaurant);
    restaurantConfirmationTokenService.saveConfirmationToken(restaurantConfirmationToken);
    return true;
  }

  public void enableRestaurantById(Long id) {
    restaurantRepository.enableRestaurantById(id);
  }

  @Transactional
  public boolean setRestaurantKYC(String email, RestaurantKYC kycRequest) {
    var restaurant = restaurantRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Restaurant with the email " + email + " not found"));

    restaurant.setName(kycRequest.getName());
    restaurant.setAddress(kycRequest.getAddress());
    restaurant.setWebsite(kycRequest.getWebsite());
    restaurant.setRegistered(kycRequest.getRegistered());
    restaurant.setBranch(kycRequest.getBranch());
    restaurant.setRangeFund(kycRequest.getRange_fund());

    return true;
  }

  public Restaurant findAppUserByEmail(String email) {
    return restaurantRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Restaurant with the email " + email + " not found"));
  }
}

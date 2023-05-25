package com.touchit.foodlify.jwttoken.restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantTokenService {

  private final RestaurantTokenRepository restaurantTokenRepository;

  public List<RestaurantToken> findAllValidTokenByRestaurantId(Long id) {
    return restaurantTokenRepository.findAllValidTokenByRestaurantId(id);
  }

  public void saveAllTokens(List<RestaurantToken> validTokens) {
    restaurantTokenRepository.saveAll(validTokens);
  }

  public void saveToken(RestaurantToken token) {
    restaurantTokenRepository.save(token);
  }
}

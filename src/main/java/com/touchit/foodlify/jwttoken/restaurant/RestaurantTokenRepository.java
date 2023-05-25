package com.touchit.foodlify.jwttoken.restaurant;

import com.touchit.foodlify.jwttoken.appuser.AppUserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantTokenRepository extends JpaRepository<RestaurantToken, Long> {

  @Query("SELECT t FROM RestaurantToken t WHERE t.restaurant.id = :restaurantId AND t.expired = false AND t.revoked = false")
  List<RestaurantToken> findAllValidTokenByRestaurantId(Long restaurantId);

  Optional<RestaurantToken> findByToken(String token);
}

package com.touchit.foodlify.otp.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface RestaurantConfirmationTokenRepository extends JpaRepository<RestaurantConfirmationToken, Long> {
    Optional<RestaurantConfirmationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE RestaurantConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    void updateConfirmedAt(String token, LocalDateTime confirmedAt);

    @Query("SELECT c FROM RestaurantConfirmationToken c WHERE c.restaurant.id = :id")
    Optional<RestaurantConfirmationToken> findRestaurantId(@Param("id") Long restaurantId);
}

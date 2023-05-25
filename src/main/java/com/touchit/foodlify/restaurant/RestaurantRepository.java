package com.touchit.foodlify.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

  Optional<Restaurant> findByEmail(String email);

  @Transactional
  @Modifying
  @Query("UPDATE Restaurant a " +
      "SET a.enabled = TRUE " +
      "WHERE a.id = ?1")
  void enableRestaurantById(Long id);

  @Query(
      """
      SELECT a FROM Restaurant a WHERE a.email = :username
      """
  )
  Optional<Restaurant> findUserByLoginUsername(String username);
}

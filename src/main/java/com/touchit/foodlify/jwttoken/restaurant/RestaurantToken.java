package com.touchit.foodlify.jwttoken.restaurant;

import com.touchit.foodlify.appuser.AppUser;
import com.touchit.foodlify.jwttoken.TokenType;
import com.touchit.foodlify.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "foodlify_restaurant_token")
public class RestaurantToken {

  @Id
  @SequenceGenerator(
      name = "foodlify_restaurant_token_sequence",
      sequenceName = "foodlify_restaurant_token_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = SEQUENCE,
      generator = "foodlify_restaurant_token_sequence"
  )
  private Integer id;
  private String token;
  @Enumerated(EnumType.STRING)
  private TokenType tokenType;
  private boolean expired;
  private boolean revoked;
  @ManyToOne
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

}

package com.touchit.foodlify.otp.restaurant;

import com.touchit.foodlify.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.SEQUENCE;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantConfirmationToken {

    @Id
    @SequenceGenerator(
            name = "restaurant_confirmation_token_sequence",
            sequenceName = "restaurant_confirmation_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "restaurant_confirmation_token_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "foodlies_restaurant_id")
    private Restaurant restaurant;

    public RestaurantConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, Restaurant restaurant) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.restaurant = restaurant;
    }
}

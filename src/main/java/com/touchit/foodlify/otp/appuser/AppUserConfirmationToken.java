package com.touchit.foodlify.otp.appuser;

import com.touchit.foodlify.appuser.AppUser;
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
public class AppUserConfirmationToken {

    @Id
    @SequenceGenerator(
            name = "app_user_confirmation_token_sequence",
            sequenceName = "app_user_confirmation_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "app_user_confirmation_token_sequence"
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
    @JoinColumn(nullable = false, name = "foodlies_app_user_id")
    private AppUser appUser;

    public AppUserConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, AppUser appUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.appUser = appUser;
    }
}

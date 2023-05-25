package com.touchit.foodlify.jwttoken.appuser;


import com.touchit.foodlify.appuser.AppUser;
import com.touchit.foodlify.jwttoken.TokenType;
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
@Table(name = "foodlify_app_user_token")
public class AppUserToken {

    @Id
    @SequenceGenerator(
            name = "foodlify_app_user_token_sequence",
            sequenceName = "foodlify_app_user_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "foodlify_app_user_token_sequence"
    )
    private Integer id;
    private String token;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    private boolean expired;
    private boolean revoked;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

}

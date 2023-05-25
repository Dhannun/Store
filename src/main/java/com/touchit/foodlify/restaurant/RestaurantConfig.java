package com.touchit.foodlify.restaurant;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class RestaurantConfig {
    private final RestaurantUserService restaurantUserService;
    private final PasswordEncoder passwordEncoder;

    public RestaurantConfig(RestaurantUserService restaurantUserService, PasswordEncoder passwordEncoder) {
        this.restaurantUserService = restaurantUserService;
        this.passwordEncoder = passwordEncoder;
    }

    /** Create an auth Provided for Spring to validate with the right details (Entity) saved in the db */
    @Bean
    @Qualifier(value = "restaurantUserAuthProvider")
    public AuthenticationProvider restaurantUserAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.restaurantUserService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /** Auth manager to validate based on the correct auth provider  */
    @Bean
    @Qualifier(value = "restaurantUserAuthManager")
    public AuthenticationManager restaurantUserAuthenticationManager (
            @Qualifier(value = "restaurantUserAuthProvider") AuthenticationProvider provider
    ) {
        return new ProviderManager(provider);
    }

}

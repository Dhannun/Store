package com.touchit.foodlify.config;

import com.touchit.foodlify.appuser.AppUser;
import com.touchit.foodlify.appuser.AppUserRepository;
import com.touchit.foodlify.exceptions.InvalidResourceException;
import com.touchit.foodlify.exceptions.ResourceNotFoundException;
import com.touchit.foodlify.restaurant.Restaurant;
import com.touchit.foodlify.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  private final AppUserRepository appUserRepository;
  private final RestaurantRepository restaurantRepository;

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String loginUsername) throws UsernameNotFoundException {

        final String type = loginUsername.substring(loginUsername.indexOf("_") + 1);
        final String username = loginUsername.substring(0, loginUsername.indexOf("_"));

        if (type.equals("USER")) {
          UserDetails appUser = appUserRepository.findUserByLoginUsername(username)
              .orElseThrow(() -> new ResourceNotFoundException("User with the username " + username + " Not Found"));
          return appUser;
        } else if (type.equals("RESTAURANT")) {
          UserDetails restaurant = restaurantRepository.findUserByLoginUsername(username)
              .orElseThrow(() -> new ResourceNotFoundException("User with the username " + username + " Not Found"));
          return restaurant;
        }
        throw new InvalidResourceException("Sorry invalid username");
      }
    };

//    return new UserDetailsService() {
//      @Override
//      public UserDetails loadUserByUsername(String loginUsername) throws UsernameNotFoundException {
//        final String type = loginUsername.substring(loginUsername.indexOf("_") + 1);
//        final String username = loginUsername.substring(0, loginUsername.indexOf("_"));
////        AppUser appUser = appUserRepository.findUserByLoginUsername(username)
////            .orElseThrow(() -> new ResourceNotFoundException("User with the username " + username + " Not Found"));
////        return appUser;
//        Restaurant restaurant = restaurantRepository.findUserByLoginUsername(username).orElseThrow(() -> new ResourceNotFoundException("User with the username " + username + " Not Found"));
//        return restaurant;
//      }
//    };
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}

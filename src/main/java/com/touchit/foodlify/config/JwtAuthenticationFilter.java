package com.touchit.foodlify.config;

import com.touchit.foodlify.jwttoken.appuser.AppUserTokenRepository;
import com.touchit.foodlify.otp.restaurant.RestaurantConfirmationTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/** TODO: validate via the URI which repository to loadUserByUsername and find by token */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService appUserDetailsService;
  private final UserDetailsService restaurantUserDetailsService;
  private final AppUserTokenRepository appUserTokenRepository;
  private final RestaurantConfirmationTokenRepository restaurantRepo;
  private final AuthenticationManager appUserAuthManager;
  private final AuthenticationManager restaurantAuthManager;

  public JwtAuthenticationFilter(
          JwtService jwtService,
          @Qualifier(value = "customAppUserService") UserDetailsService appUserDetailsService,
          @Qualifier(value = "restaurantUserService") UserDetailsService restaurantUserDetailsService,
          AppUserTokenRepository appUserTokenRepository,
          RestaurantConfirmationTokenRepository restaurantRepo,
          @Qualifier(value = "appUserAuthManager") AuthenticationManager authenticationManager,
          @Qualifier(value = "restaurantUserAuthManager") AuthenticationManager restaurantAuthManager
  ) {
    this.jwtService = jwtService;
    this.appUserDetailsService = appUserDetailsService;
    this.restaurantUserDetailsService = restaurantUserDetailsService;
    this.appUserTokenRepository = appUserTokenRepository;
    this.restaurantRepo = restaurantRepo;
    this.appUserAuthManager = authenticationManager;
    this.restaurantAuthManager = restaurantAuthManager;
  }

  @Override
  protected void doFilterInternal(
     @NonNull HttpServletRequest request,
     @NonNull HttpServletResponse response,
     @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;

    if (authHeader == null || !authHeader.startsWith("Bearer ")){
      filterChain.doFilter(request, response);
      return;
    }

    jwt = authHeader.substring(7);
    userEmail = jwtService.extractUsername(jwt); // todo: extract user email from JWT token
    helper(request.getRequestURI(), jwt, userEmail);

    filterChain.doFilter(request, response);
  }

  private void helper (String uri, String jwt, String userEmail) {
    switch (uri) {

      case "/api/v1/auth/restaurant" -> {
        UserDetails userDetails = this.restaurantUserDetailsService.loadUserByUsername(userEmail);
        if (jwtService.isTokenValid(jwt, userDetails) && this.restaurantRepo.findByToken(jwt).isPresent()) {
          SecurityContextHolder
                  .getContext()
                  .setAuthentication(this.restaurantAuthManager.authenticate(
                          new UsernamePasswordAuthenticationToken(
                                  userDetails.getUsername(),
                                  userDetails.getPassword()
                          )
                  ));
        }
//        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//          // End of If
//        }
      }

      case "/api/v1/auth/user" -> {
        UserDetails userDetails = this.appUserDetailsService.loadUserByUsername(userEmail);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

          if (jwtService.isTokenValid(jwt, userDetails) && appUserTokenRepository.findByToken(jwt).isPresent()) {
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(this.appUserAuthManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userDetails.getUsername(),
                                    userDetails.getPassword()
                            )
                    ));
          }
          // End of If
        }
        break;
      }

      default -> throw new RuntimeException("");
    }

  }



}

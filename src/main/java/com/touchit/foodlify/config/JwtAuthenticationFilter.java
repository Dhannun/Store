package com.touchit.foodlify.config;

import com.touchit.foodlify.jwttoken.appuser.AppUserTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/** TODO: validate via the URI which repository to loadUserByUsername and find by token */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final UserDetailsService restaurantUserDetailsService;
  private final AppUserTokenRepository appUserTokenRepository;

  public JwtAuthenticationFilter(
          JwtService jwtService,
          @Qualifier(value = "customAppUserService") UserDetailsService userDetailsService,
          @Qualifier(value = "restaurantUserService") UserDetailsService restaurantUserDetailsService,
          AppUserTokenRepository appUserTokenRepository
  ) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.restaurantUserDetailsService = restaurantUserDetailsService;
    this.appUserTokenRepository = appUserTokenRepository;
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
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

      var isTokenValid = appUserTokenRepository.findByToken(jwt)
              .map(token -> !token.isExpired() && !token.isRevoked()).orElse(false);

      if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);

      }
      // End of If
    }
    filterChain.doFilter(request, response);
  }



}

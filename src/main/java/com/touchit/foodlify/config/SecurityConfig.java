package com.touchit.foodlify.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;


import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()

        .authorizeHttpRequests()
        .requestMatchers(
            "/api/v1/auth/user/**",
            "api/v1/auth/restaurant/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
        )
        .permitAll()
        .anyRequest()
        .authenticated()

        .and()
        .sessionManagement()
        .sessionCreationPolicy(STATELESS)

        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

        .logout()
        .addLogoutHandler(logoutHandler)
        .logoutUrl("/api/v1/auth/logout")
        .logoutSuccessHandler(
            (request, response, authentication) -> SecurityContextHolder.clearContext()
        )
    ;

    return http.build();
  }
}

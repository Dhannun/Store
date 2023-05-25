package com.touchit.foodlify.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final LogoutHandler logoutHandler;

    private String[] publicRoutes() {
        return new String[] {
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
        };
    }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // No need for auth provider because you because we injected a Bean
    return http
            .cors(AbstractHttpConfigurer::disable) // Disable CORS
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(publicRoutes()).permitAll();
                auth.anyRequest().authenticated();
            })
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(STATELESS))
            .addFilterBefore(this.jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logoutConfig -> logoutConfig
                    .addLogoutHandler(this.logoutHandler)
                    .logoutUrl("/api/v1/auth/logout")
                    .logoutSuccessHandler((request, response, authentication) ->
                            SecurityContextHolder.clearContext()
                    )
            )
            .build();
  }
}

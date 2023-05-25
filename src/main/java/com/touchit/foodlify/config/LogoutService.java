package com.touchit.foodlify.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.touchit.foodlify.dto.response.MessageResponse;
import com.touchit.foodlify.jwttoken.appuser.AppUserTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final AppUserTokenRepository appUserTokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        jwt = authHeader.substring(7);

        var storedToken = appUserTokenRepository.findByToken(jwt).orElse(null);

        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            appUserTokenRepository.save(storedToken);
            response.setStatus(OK.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            var responseBody = MessageResponse.successMessage("Logged our Successfully");
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
                return;
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}

package com.touchit.foodlify.appuser;

import com.touchit.foodlify.exceptions.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "customAppUserService")
public class CustomAppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public CustomAppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Return Username or email not found
        return this.appUserRepository.findUserByLoginUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("AppUser username or email not found"));
    }
}

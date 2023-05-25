package com.touchit.foodlify.restaurant;

import com.touchit.foodlify.exceptions.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "restaurantUserService")
public class RestaurantUserService implements UserDetailsService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantUserService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.restaurantRepository.findUserByLoginUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantUser username or email not found"));
    }

}

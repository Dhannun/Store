package com.touchit.foodlify.appuser;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppUserConfig {

    private final CustomAppUserService customAppUserService;
    private final PasswordEncoder passwordEncoder;

    public AppUserConfig(CustomAppUserService customAppUserService, PasswordEncoder passwordEncoder) {
        this.customAppUserService = customAppUserService;
        this.passwordEncoder = passwordEncoder;
    }

    /** Create an auth Provided for Spring to validate with the right details (Entity) saved in the db */
    @Bean
    @Primary
    @Qualifier(value = "appUserAuthProvider")
    public AuthenticationProvider appUserAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.customAppUserService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * Auth manager to validate based on the correct auth provider. Note I am setting this as the primary you can
     * interchange with bean In RestaurantCongfig
     *
     * @param provider
     * @return AuthenticationManager
     * */
    @Bean
    @Primary
    @Qualifier(value = "appUserAuthManager")
    public AuthenticationManager appUserAuthenticationManager(
            @Qualifier(value = "appUserAuthProvider") AuthenticationProvider provider
    ) {
        return new ProviderManager(provider);
    }

}

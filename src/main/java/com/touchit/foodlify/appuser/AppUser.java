package com.touchit.foodlify.appuser;

import com.touchit.foodlify.jwttoken.appuser.AppUserToken;
import com.touchit.foodlify.univrsal.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "foodlify_user")
public class AppUser implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "foodlify_user_sequence",
            sequenceName = "foodlify_user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "foodlify_user_sequence"
    )
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean locked;
    private Boolean enabled;

    @OneToMany(mappedBy = "appUser")
    private List<AppUserToken> appUserTokens;

    public AppUser(String fullName, String email, String phoneNumber, String password, Role role, Boolean locked, Boolean enabled) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.locked = locked;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !getLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getEnabled();
    }
}

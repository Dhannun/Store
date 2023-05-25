package com.touchit.foodlify.restaurant;

import com.touchit.foodlify.univrsal.Role;
import com.touchit.foodlify.jwttoken.restaurant.RestaurantToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "foodlify_restaurant")
public class Restaurant implements UserDetails {
  @Id
  @SequenceGenerator(
      name = "foodlify_restaurant_sequence",
      sequenceName = "foodlify_restaurant_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = SEQUENCE,
      generator = "foodlify_restaurant_sequence"
  )
  private Long id;
  private String name;
  private String address;
  private String email;
  private String password;
  private String website;
  private Boolean registered;
  private Integer branch;
  private Long rangeFund;
  private String restaurantId;
  private Boolean locked;
  private Boolean enabled;
  private Role role;
//
  @ManyToOne
  @JoinColumn(name = "person_id")
  private RestaurantPerson restaurantPerson;

  @OneToMany(mappedBy = "restaurant")
  private List<RestaurantToken> restaurantTokens;

  public Restaurant(String name, String address, String email, String password, String website, Boolean registered, Integer branch, Long rangeFund, String restaurantId, Boolean locked, Boolean enabled, Role role) {
    this.name = name;
    this.address = address;
    this.email = email;
    this.password = password;
    this.website = website;
    this.registered = registered;
    this.branch = branch;
    this.rangeFund = rangeFund;
    this.restaurantId = restaurantId;
    this.locked = locked;
    this.enabled = enabled;
    this.role = role;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return null;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !getLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return getEnabled();
  }
}

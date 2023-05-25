package com.touchit.foodlify.restaurant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "foodlify_restaurant_person")
public class RestaurantPerson {

  @Id
  @SequenceGenerator(
      name = "foodlify_restaurant_person_sequence",
      sequenceName = "foodlify_restaurant_person_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = SEQUENCE,
      generator = "foodlify_restaurant_person_sequence"
  )
  private Long id;
  private String firstName;
  private String surname;
  private String email;
  private String phoneNumber;
  private String identity;
  private String identityFile;

  @OneToMany(mappedBy = "restaurantPerson")
  private List<Restaurant> restaurant;

  public RestaurantPerson(String firstName, String surname, String email, String phoneNumber, String identity, String identityFile) {
    this.firstName = firstName;
    this.surname = surname;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.identity = identity;
    this.identityFile = identityFile;
  }
}

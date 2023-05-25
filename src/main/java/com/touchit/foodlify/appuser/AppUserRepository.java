package com.touchit.foodlify.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
  Optional<AppUser> findByEmail(String email);

  Optional<AppUser> findByPhoneNumber(String phoneNumber);

  /*@Transactional
  @Modifying
  @Query("UPDATE AppUser a " +
      "SET a.enabled = TRUE " +
      "WHERE a.email = ?1")
  void enableAppUserByEmail(String email);

  @Transactional
  @Modifying
  @Query("UPDATE AppUser a " +
      "SET a.enabled = TRUE " +
      "WHERE a.phoneNumber = ?1")
  void enableAppUserByPhoneNumber(String phoneNumber);*/

  @Transactional
  @Modifying
  @Query("UPDATE AppUser a " +
      "SET a.enabled = TRUE " +
      "WHERE a.id = ?1")
  void enableAppUserById(Long id);

  @Query(
      """
      SELECT a FROM AppUser a WHERE a.email = :username OR a.phoneNumber = :username
      """
  )
  Optional<AppUser> findUserByLoginUsername(String username);
}

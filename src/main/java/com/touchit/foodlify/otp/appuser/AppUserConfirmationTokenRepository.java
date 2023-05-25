package com.touchit.foodlify.otp.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserConfirmationTokenRepository extends JpaRepository<AppUserConfirmationToken, Long> {
    Optional<AppUserConfirmationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE AppUserConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    void updateConfirmedAt(String token, LocalDateTime confirmedAt);

    @Query("SELECT c FROM AppUserConfirmationToken c WHERE c.appUser.id = :id")
    Optional<AppUserConfirmationToken> findByUserId(@Param("id") Long appUserId);
}

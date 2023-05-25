package com.touchit.foodlify.jwttoken.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppUserTokenRepository extends JpaRepository<AppUserToken, Integer> {

//    SELECT t FROM Token t INNER JOIN AppUser u ON t.app_user.id = u.id
//    WHERE u.id = :userId AND (t.expired = false OR t.revoked = false)
    @Query("SELECT t FROM AppUserToken t WHERE t.appUser.id = :userId AND t.expired = false AND t.revoked = false")
    List<AppUserToken> findAllValidTokenByUser(Long userId);

    Optional<AppUserToken> findByToken(String token);
}

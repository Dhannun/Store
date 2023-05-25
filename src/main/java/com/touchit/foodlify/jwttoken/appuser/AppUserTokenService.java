package com.touchit.foodlify.jwttoken.appuser;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AppUserTokenService {

  private final AppUserTokenRepository appUserTokenRepository;

  public List<AppUserToken> findAllValidTokenByUser(Long id) {
    return appUserTokenRepository.findAllValidTokenByUser(id);
  }

  public void saveAllTokens(List<AppUserToken> validAppUserTokens) {
    appUserTokenRepository.saveAll(validAppUserTokens);
  }

  public void saveToken(AppUserToken appUserToken) {
    appUserTokenRepository.save(appUserToken);
  }
}

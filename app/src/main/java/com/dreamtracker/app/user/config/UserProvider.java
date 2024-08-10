package com.dreamtracker.app.user.config;

import com.dreamtracker.app.infrastructure.auth.JwtService;
import com.dreamtracker.app.user.domain.model.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class UserProvider implements CurrentUserProvider {

  private static final Logger logger =
      LoggerFactory.getLogger(UserProvider.class);

//  @Override
//  public UUID getCurrentUser() {
//    return UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
//  }

  @Override
  public UUID getCurrentUser() {
    Authentication test = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) test.getPrincipal();
    logger.trace("User: {}", user);
    return user.getUuid();
  }
}

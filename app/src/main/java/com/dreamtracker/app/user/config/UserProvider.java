package com.dreamtracker.app.user.config;

import com.dreamtracker.app.user.domain.model.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProvider implements CurrentUserProvider {

  private static final Logger logger =
      LoggerFactory.getLogger(UserProvider.class);
  
  @Override
  public UUID getCurrentUser() {
    Authentication test = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) test.getPrincipal();
    logger.trace("User: {}", user);
    return user.getUuid();
  }
}

package com.dreamtracker.app.infrastructure.auth;

import com.dreamtracker.app.user.adapters.api.UserResponse;
import com.dreamtracker.app.user.domain.model.User;
import com.dreamtracker.app.user.domain.ports.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepositoryPort userRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

  public UserResponse register(RegistrationRequest input) {
    var user =
        User.builder()
            .email(input.email())
            .fullName(input.fullName())
            .password(passwordEncoder.encode(input.password()))
            .build();

    var userSavedToDB = userRepository.save(user);
    return mapToUserResponse(userSavedToDB);
  }

  public AuthenticationResponse login(LoginRequest input) {
    logger.debug("Login request received: " + input.toString());
    try {
      logger.debug("Attempting authentication for user: " + input.email());
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(input.email(), input.password()));
      logger.debug("Authentication successful for user: " + input.email());
    } catch (Exception e) {
      logger.error("Authentication failed for user: " + input.email(), e);
      throw e;
    }

    var authenticatedUser = userRepository.findByEmail(input.email()).orElseThrow();
    logger.debug(authenticatedUser.toString());

    return mapToAuthenticationResponse(authenticatedUser);
  }

  private AuthenticationResponse mapToAuthenticationResponse(User user) {
    var token = jwtService.generateToken(user);
    logger.debug("Token generated: " + token);
    return AuthenticationResponse.builder().token(token).build();
  }

  private UserResponse mapToUserResponse(User user) {
    return UserResponse.builder().uuid(user.getUuid()).build();
  }
}

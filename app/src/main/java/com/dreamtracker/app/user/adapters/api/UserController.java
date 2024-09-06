package com.dreamtracker.app.user.adapters.api;

import com.dreamtracker.app.infrastructure.auth.*;
import com.dreamtracker.app.user.domain.ports.UserService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
@Data
public class UserController {

  private final UserService userService;
  private final AuthenticationService authenticationService;
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @PostMapping("/auth/register")
  public ResponseEntity<UserResponse> register(
      @RequestBody RegistrationRequest registrationRequest) {
    return new ResponseEntity<>(
        authenticationService.register(registrationRequest), HttpStatus.CREATED);
  }

  @PostMapping("/auth/login")
  public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
    logger.debug("Login request received");
    return new ResponseEntity<>(authenticationService.login(loginRequest), HttpStatus.OK);
  }

  @PostMapping("/auth/reset-password-request")
  public ResponseEntity<PasswordResetResponse>requestPasswordReset(
      @RequestBody EnterPasswordResetRequest resetRequest) {
    return new ResponseEntity<>(userService.requestPasswordReset(resetRequest), HttpStatus.ACCEPTED);
  }

  @PostMapping("/auth/reset-password")
  public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetRequest resetRequest) {
    userService.resetPassword(resetRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/auth/confirm-password")
  public ResponseEntity<UserResponse>confirmRegistration(@RequestBody RegistrationConfirmation registrationConfirmation){
    return new ResponseEntity<>(userService.confirmAccount(UUID.fromString(registrationConfirmation.userUUID())),HttpStatus.OK);
  }
}

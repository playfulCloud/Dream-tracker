package com.dreamtracker.app.application.controller;

import com.dreamtracker.app.domain.response.UserResponse;
import com.dreamtracker.app.service.UserService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Data
public class UserController {

  private final UserService userService;

  @PostMapping("/seed")
  public ResponseEntity<UserResponse> createSampleUserForTesting() {
    return ResponseEntity.ok(userService.createSampleUser());
  }
}

package com.dreamtracker.app.infrastructure.auth;


import lombok.Builder;

@Builder
public record AuthenticationResponse(String token) {}

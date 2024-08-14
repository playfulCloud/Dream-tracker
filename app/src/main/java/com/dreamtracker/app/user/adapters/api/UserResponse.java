package com.dreamtracker.app.user.adapters.api;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UserResponse(UUID uuid, String name, String surname, String fullName, String email) {}

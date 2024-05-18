package com.dreamtracker.app.user.adapters.api;

import lombok.Builder;

import java.util.UUID;


@Builder
public record UserResponse(UUID uuid, String name, String surname) {}

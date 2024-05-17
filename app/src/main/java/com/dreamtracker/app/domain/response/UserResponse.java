package com.dreamtracker.app.domain.response;

import lombok.Builder;

import java.util.UUID;


@Builder
public record UserResponse(UUID uuid, String name, String surname) {}

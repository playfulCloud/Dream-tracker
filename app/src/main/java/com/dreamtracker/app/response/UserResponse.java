package com.dreamtracker.app.response;

import lombok.Builder;

import java.util.UUID;


@Builder
public record UserResponse(UUID uuid, String name, String surname) {}

package com.dreamtracker.app.habit.adapters.api;


import lombok.Builder;

import java.util.UUID;

@Builder
public record CategoryResponse(UUID id, String name) {}

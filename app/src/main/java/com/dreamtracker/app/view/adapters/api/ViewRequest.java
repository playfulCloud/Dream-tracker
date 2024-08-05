package com.dreamtracker.app.view.adapters.api;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ViewRequest(UUID id, String name, boolean habits, boolean stats, boolean goals, String description) {}

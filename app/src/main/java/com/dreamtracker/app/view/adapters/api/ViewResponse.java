package com.dreamtracker.app.view.adapters.api;


import lombok.Builder;

import java.util.UUID;

@Builder
public record ViewResponse(String name, UUID habitUUID) {}

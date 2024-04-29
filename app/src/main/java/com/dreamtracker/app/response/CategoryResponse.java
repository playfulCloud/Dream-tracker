package com.dreamtracker.app.response;


import lombok.Builder;

import java.util.UUID;

@Builder
public record CategoryResponse(UUID id, String name) {}

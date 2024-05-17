package com.dreamtracker.app.domain.response;


import lombok.Builder;

import java.util.UUID;

@Builder
public record CategoryResponse(UUID id, String name) {}

package com.dreamtracker.app.user.adapters.api;

import lombok.Builder;

@Builder
public record ActivationRequest(
        boolean habitEnabled, boolean goalEnabled, boolean statsEnabled, boolean chartsEnabled
) {}

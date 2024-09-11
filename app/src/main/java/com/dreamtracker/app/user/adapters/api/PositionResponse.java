package com.dreamtracker.app.user.adapters.api;

import lombok.Builder;

@Builder
public record PositionResponse(
    int habitX, int habitY,int goalX, int goalY, int statX, int statY, int chartX, int chartY, boolean habitEnabled, boolean goalEnabled, boolean statsEnabled, boolean chartsEnabled
) {}

package com.dreamtracker.app.user.adapters.api;

import lombok.Builder;

@Builder
public record PositionChangeRequest(
        int habitX, int habitY,int goalX, int goalY, int statX, int statY, int chartX, int chartY
) {}

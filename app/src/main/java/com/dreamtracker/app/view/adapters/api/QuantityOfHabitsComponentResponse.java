package com.dreamtracker.app.view.adapters.api;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class QuantityOfHabitsComponentResponse extends StatsComponentResponse {
    private final int done;
    private final int undone;
    private final String trend;
}

package com.dreamtracker.app.view.adapters.api;


import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class StreakComponentResponse extends StatsComponentResponse{
    private final int longest;
    private final int actual;
}

package com.dreamtracker.app.view.adapters.api;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class StreakComponentResponse extends StatsComponentResponse{
    private final int longest;
    private final int actual;
}

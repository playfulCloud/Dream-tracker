package com.dreamtracker.app.view.adapters.api;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class SingleDayComponentResponse extends StatsComponentResponse{
    private final int most;
    private final int actual;
    private final String date;
}

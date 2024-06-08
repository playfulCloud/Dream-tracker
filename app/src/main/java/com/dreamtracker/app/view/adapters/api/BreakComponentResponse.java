package com.dreamtracker.app.view.adapters.api;

import lombok.Builder;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Builder
public class BreakComponentResponse extends StatsComponentResponse {
    private final double averageBreak;
}

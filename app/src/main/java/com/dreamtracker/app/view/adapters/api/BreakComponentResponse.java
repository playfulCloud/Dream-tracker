package com.dreamtracker.app.view.adapters.api;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Builder
@Getter
public class BreakComponentResponse extends StatsComponentResponse {
    private final double averageBreak;
}

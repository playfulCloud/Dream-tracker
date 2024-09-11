package com.dreamtracker.app.habit.domain.model;

import java.time.LocalDate;

public record ChartResponse(LocalDate date, int count, int all) {}

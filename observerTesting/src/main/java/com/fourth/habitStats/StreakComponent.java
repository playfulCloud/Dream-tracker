package com.fourth.habitStats;

import com.fourth.aggregates.StreakAggregate;
import com.fourth.componentResponse.ComponentResponseContainer;
import com.fourth.componentResponse.StreakResponse;

public class StreakComponent extends StatsComponent{

    @Override
    public ComponentResponseContainer update(String name) {
        System.out.println(name);
        StreakAggregate streakAggregate = new StreakAggregate();
        return new StreakResponse(streakAggregate.doSome());
    }
}

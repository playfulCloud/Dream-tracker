package com.fourth.habitStats;

import com.fourth.componentResponse.ComponentResponseContainer;
import com.fourth.componentResponse.StreakResponse;

public class StreakComponent extends StatsComponent{

    @Override
    public ComponentResponseContainer update(String name) {
        System.out.println(name);
        return new StreakResponse();
    }
}

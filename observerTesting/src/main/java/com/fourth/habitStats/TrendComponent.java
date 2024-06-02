package com.fourth.habitStats;

import com.fourth.aggregates.TrendAggregate;
import com.fourth.componentResponse.ComponentResponseContainer;
import com.fourth.componentResponse.TrendResponse;

public class TrendComponent extends StatsComponent{
    @Override
    public ComponentResponseContainer update(String name) {
        System.out.println(name);
        TrendAggregate trendAggregate = new TrendAggregate();
        return new TrendResponse(trendAggregate.doSome());
    }
}

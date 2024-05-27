package com.fourth.habitStats;

import com.fourth.componentResponse.AverageBreakResponse;
import com.fourth.componentResponse.ComponentResponseContainer;
import com.fourth.observer.Observer;

import java.sql.SQLOutput;

public class AverageBreakComponent extends StatsComponent implements Observer{
    @Override
    public ComponentResponseContainer update(String name) {
        System.out.println(name);
        return new AverageBreakResponse();
    }
}

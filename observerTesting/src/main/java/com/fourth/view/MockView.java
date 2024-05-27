package com.fourth.view;

import com.fourth.componentResponse.ComponentResponseContainer;
import com.fourth.habitStats.StatsComponent;
import com.fourth.observable.Observable;
import com.fourth.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class MockView implements Observable {

    List<StatsComponent>followedStats = new ArrayList<>();
    List<ComponentResponseContainer> viewOfStats = new ArrayList<>();

    @Override
    public boolean addObserver(StatsComponent statsComponent) {
        return followedStats.add(statsComponent);
    }

    @Override
    public boolean removeObserver(StatsComponent statsComponent) {
        return followedStats.remove(statsComponent);
    }

    @Override
    public boolean notifyObservers() {
        for (StatsComponent statsComponent : followedStats) {
            new Thread(() -> {
                ComponentResponseContainer response = statsComponent.update(statsComponent.getClass().getName());
                synchronized (viewOfStats) {
                    viewOfStats.add(response);
                }
            }).start();
        }
        return true;
    }

}

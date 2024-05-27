package com.fourth.observable;

import com.fourth.habitStats.StatsComponent;
import com.fourth.observer.Observer;

public interface Observable {
    boolean addObserver(StatsComponent statsComponent);
    boolean removeObserver(StatsComponent statsComponent);
    boolean notifyObservers();
}

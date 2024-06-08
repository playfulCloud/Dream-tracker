package com.dreamtracker.app.view.domain.model.aggregateManagers;


import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.config.StatsAggregatorObserver;
import com.dreamtracker.app.view.config.StatsAggregatorObservable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StatsAggregator implements StatsAggregatorObservable {

    private List<StatsAggregatorObserver> observers;

    @Autowired
    public StatsAggregator(List<StatsAggregatorObserver> observers) {
        this.observers = observers;
    }

    @Override
    public boolean initializeAggregates(UUID habitUUID) {
       this.observers.forEach(aggregator -> aggregator.initializeAggregate(habitUUID));
       return true;
    }

    @Override
    public Page<StatsComponentResponse> requestStatsUpdated(UUID habitUUID, HabitTrackingRequest habitTrackingRequest) {
    this.observers.forEach(x -> System.out.println(x.getClass().getSimpleName()));
        return null;
    }

    @Override
    public Page<StatsComponentResponse> getAggregates(UUID habitUUID) {
        var responsesToMap = getResponsesFromAggregates(habitUUID);
        return mapToPageResponse(responsesToMap);
    }

    private Page<StatsComponentResponse> mapToPageResponse(List<StatsComponentResponse> responses){
       return new Page<>(responses);
    }

    private List<StatsComponentResponse> getResponsesFromAggregates(UUID habitUUID){
        var aggregatesResponses = new ArrayList<StatsComponentResponse>();
       for(StatsAggregatorObserver statsAggregatorObserver : observers){
           aggregatesResponses.add(statsAggregatorObserver.getAggregate(habitUUID));
       }
       return aggregatesResponses;
    }

}

package com.dreamtracker.app.view.domain.model.aggregateManagers;


import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.config.StatsAggregatorObservable;
import com.dreamtracker.app.view.config.StatsAggregatorObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public Page<StatsComponentResponse> requestStatsUpdated(UUID habitUUID, HabitTrackResponse habitTrackResponse) {
        var responsesToMap = triggerUpdateAndGetResponse(habitUUID,habitTrackResponse);
        return mapToPageResponse(responsesToMap);
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


    private List<StatsComponentResponse>triggerUpdateAndGetResponse(UUID habitUUID,HabitTrackResponse trackResponse){
        var aggregatesResponses = new ArrayList<StatsComponentResponse>();
        for(StatsAggregatorObserver statsAggregatorObserver : observers){
            aggregatesResponses.add(statsAggregatorObserver.updateAggregate(habitUUID,trackResponse));
        }
        return aggregatesResponses;
    }

}

package com.dreamtracker.app.view.domain.model.aggregate;


import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.view.adapters.api.CombinedComponentResponse;
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
    public CombinedComponentResponse requestStatsUpdated(UUID habitUUID, HabitTrackResponse habitTrackResponse) {
        var responsesToMap = triggerUpdateAndGetResponse(habitUUID,habitTrackResponse);
        return mapToPageResponse(responsesToMap);
    }

    @Override
    public CombinedComponentResponse getAggregates(UUID habitUUID) {
        var responsesToMap = getResponsesFromAggregates(habitUUID);
        return mapToPageResponse(responsesToMap);
    }

    private CombinedComponentResponse mapToPageResponse(List<StatsComponentResponse> responses){
        CombinedComponentResponse combinedComponentResponse = new CombinedComponentResponse();
        for(StatsComponentResponse componentResponse : responses){
            componentResponse.combineResponse(combinedComponentResponse);
        }
       return combinedComponentResponse;
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

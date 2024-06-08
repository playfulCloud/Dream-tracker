package com.dreamtracker.app.view.domain.ports;

import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.adapters.api.ViewRequest;
import com.dreamtracker.app.view.adapters.api.ViewResponse;
import com.dreamtracker.app.view.domain.model.View;
import com.dreamtracker.app.view.domain.model.aggregateManagers.StatsAggregator;
import lombok.RequiredArgsConstructor;

//TODO: this whole service will implemented in iteration 3
@RequiredArgsConstructor
public class DomainViewService implements ViewService {

  private final ViewRepositoryPort viewRepositoryPort;
  private final StatsAggregator statsAggregator;

  @Override
  public Page<StatsComponentResponse> getStatsComponent(String viewName) {
    var foundView =
        viewRepositoryPort
            .findByName(viewName)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    return statsAggregator.getAggregates(foundView.getHabitUUID());
  }

  @Override
  public ViewResponse createView(ViewRequest viewRequest) {
    var viewToSave =
        View.builder().name(viewRequest.name()).habitUUID(viewRequest.habitUUID()).build();
    var viewSavedToDB = viewRepositoryPort.save(viewToSave);
    return mapToResponse(viewSavedToDB);
  }

  private ViewResponse mapToResponse(View view) {
    return ViewResponse.builder().name(view.getName()).habitUUID(view.getHabitUUID()).build();
  }
}

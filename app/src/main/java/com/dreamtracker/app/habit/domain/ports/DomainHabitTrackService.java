package com.dreamtracker.app.habit.domain.ports;

import com.dreamtracker.app.goal.domain.model.Goal;
import com.dreamtracker.app.goal.domain.ports.DomainGoalService;
import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.habit.domain.model.HabitTrack;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.view.domain.model.aggregate.StatsAggregator;
import jakarta.transaction.Transactional;
import java.time.*;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DomainHabitTrackService implements HabitTrackService {

  private final HabitTrackRepositoryPort habitTrackRepositoryPort;
  private final HabitRepositoryPort habitRepositoryPort;
  private final DomainGoalService domainGoalService;
  private final StatsAggregator statsAggregator;
  private final Clock clock;


  @Override
  public Page<HabitTrackResponse> getAllTracksOfHabit(UUID id) {
    var listOfTracks = habitTrackRepositoryPort.findByHabitUUID(id);
    var listOfTracksResponses = listOfTracks.stream().map(this::mapToResponse).toList();
    Page<HabitTrackResponse> habitTrackResponsePage = new Page<>(listOfTracksResponses);
    return habitTrackResponsePage;
  }

  @Override
  @Transactional
  public HabitTrackResponse trackTheHabit(HabitTrackingRequest habitTrackingRequest) {

    var habitToUpdateTracking =
        habitRepositoryPort
            .findById(habitTrackingRequest.habitId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    var actualDate = Instant.now(clock);

    var track =
        HabitTrack.builder()
            .date(actualDate)
            .status(habitTrackingRequest.status())
            .habitUUID(habitToUpdateTracking.getId())
            .build();


    var trackSavedToDB = habitTrackRepositoryPort.save(track);
    var habitTrackResponse = mapToResponse(trackSavedToDB);
    statsAggregator.requestStatsUpdated(habitToUpdateTracking.getId(), habitTrackResponse);

    if (trackSavedToDB.getStatus().equals("DONE")) {
      updateGoalProgress(habitToUpdateTracking.getGoals());
    }
    return habitTrackResponse;
  }

  private HabitTrackResponse mapToResponse(HabitTrack habitTrack) {
    return HabitTrackResponse.builder()
        .date(habitTrack.getDate())
        .status(habitTrack.getStatus())
        .build();
  }

  private void updateGoalProgress(List<Goal> goals) {
    for (Goal goal : goals) {
      domainGoalService.increaseCompletionCount(goal.getUuid());
    }
  }
}

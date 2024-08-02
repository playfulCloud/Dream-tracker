package com.dreamtracker.app.habit.domain.ports;

import com.dreamtracker.app.goal.domain.model.Goal;
import com.dreamtracker.app.goal.domain.ports.DomainGoalService;
import com.dreamtracker.app.goal.domain.ports.GoalRepositoryPort;
import com.dreamtracker.app.goal.domain.ports.GoalService;
import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.habit.domain.model.HabitStatus;
import com.dreamtracker.app.habit.domain.model.HabitTrack;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.view.domain.model.aggregate.StatsAggregator;

import java.time.*;
import java.time.format.DateTimeFormatter;
import jakarta.transaction.Transactional;
import java.time.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class DomainHabitTrackService implements HabitTrackService {

  private final HabitTrackRepositoryPort habitTrackRepositoryPort;
  private final HabitRepositoryPort habitRepositoryPort;
  private final StatsAggregator statsAggregator;
  private final Clock clock;
  private final GoalService domainGoalService;


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
      habitToUpdateTracking.setStatus(HabitStatus.COOLDOWN.toString());
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
    List<Goal> goalsCopy = new ArrayList<>(goals);
    for (Goal goal : goalsCopy) {
      domainGoalService.increaseCompletionCount(goal.getUuid());
    }
  }
}

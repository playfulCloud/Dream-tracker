package com.dreamtracker.app.habit.domain.ports;

import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.habit.domain.model.HabitTrack;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.infrastructure.utils.DateService;
import com.dreamtracker.app.view.domain.model.aggregateManagers.StatsAggregator;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DomainHabitTrackService implements HabitTrackService {

  private final HabitTrackRepositoryPort habitTrackRepositoryPort;
  private final HabitRepositoryPort habitRepositoryPort;
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
  public HabitTrackResponse trackTheHabit(HabitTrackingRequest habitTrackingRequest) {

    var habitToUpdateTracking =
        habitRepositoryPort
            .findById(habitTrackingRequest.habitId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    ZonedDateTime date = ZonedDateTime.now(clock);
    String formattedDate = date.format(DateTimeFormatter.ISO_DATE_TIME);

    var track =
        HabitTrack.builder()
            .date(formattedDate)
            .status(habitTrackingRequest.status())
            .habitUUID(habitToUpdateTracking.getId())
            .build();

    var trackSavedToDB = habitTrackRepositoryPort.save(track);
    var habitTrackResponse = mapToResponse(trackSavedToDB);
    statsAggregator.requestStatsUpdated(habitToUpdateTracking.getId(), habitTrackResponse);
    return habitTrackResponse;
  }

  private HabitTrackResponse mapToResponse(HabitTrack habitTrack) {
    return HabitTrackResponse.builder()
        .date(habitTrack.getDate())
        .status(habitTrack.getStatus())
        .build();
  }
}

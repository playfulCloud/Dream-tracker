package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.entity.HabitTrack;
import com.dreamtracker.app.exception.EntityNotFoundException;
import com.dreamtracker.app.exception.EntitySaveException;
import com.dreamtracker.app.exception.ExceptionMessages;
import com.dreamtracker.app.repository.HabitRepository;
import com.dreamtracker.app.repository.HabitTrackRepository;
import com.dreamtracker.app.request.HabitTrackingRequest;
import com.dreamtracker.app.response.HabitTrackResponse;
import com.dreamtracker.app.response.Page;
import com.dreamtracker.app.service.HabitTrackService;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HabitTrackServiceImpl implements HabitTrackService {

  private final HabitTrackRepository habitTrackRepository;
  private final HabitRepository habitRepository;
  private final Clock clock;


  @Override
  public Page<HabitTrackResponse> getAllTracksOfHabit(UUID id) {
    var listOfTracks = habitTrackRepository.findByHabitUUID(id);
    var listOfTracksResponses = listOfTracks.stream().map(this::mapToResponse).toList();
    Page<HabitTrackResponse> habitTrackResponsePage = new Page<>(listOfTracksResponses);
    return habitTrackResponsePage;
  }

  @Override
  public HabitTrackResponse trackTheHabit(HabitTrackingRequest habitTrackingRequest) {

    var habitToUpdateTracking =
        habitRepository
            .findById(habitTrackingRequest.habitId())
            .orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    ZonedDateTime date = ZonedDateTime.now(clock);
    String formattedDate = date.format(DateTimeFormatter.ISO_DATE_TIME);

    var track =
        HabitTrack.builder()
            .date(formattedDate)
            .status(habitTrackingRequest.status())
            .habitUUID(habitToUpdateTracking.getId())
            .build();

    var trackSavedToDB = habitTrackRepository.save(track);
    return mapToResponse(trackSavedToDB);
  }

  private HabitTrackResponse mapToResponse(HabitTrack habitTrack) {
    return HabitTrackResponse.builder()
        .date(habitTrack.getDate())
        .status(habitTrack.getStatus())
        .build();
  }
}

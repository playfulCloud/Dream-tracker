package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.entity.Habit;
import com.dreamtracker.app.entity.HabitTrack;
import com.dreamtracker.app.exception.EntitySaveException;
import com.dreamtracker.app.exception.ExceptionMessages;
import com.dreamtracker.app.repository.HabitRepository;
import com.dreamtracker.app.repository.HabitTrackRepository;
import com.dreamtracker.app.request.HabitTrackingRequest;
import com.dreamtracker.app.response.HabitTrackResponse;
import com.dreamtracker.app.response.Page;
import com.dreamtracker.app.security.CurrentUserProvider;
import com.dreamtracker.app.service.HabitService;
import com.dreamtracker.app.service.HabitTrackService;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import com.dreamtracker.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HabitTrackServiceImpl implements HabitTrackService {

  private final HabitTrackRepository habitTrackRepository;
  private final HabitService habitService;
  private final CurrentUserProvider currentUserProvider;
  private final UserService userService;
  private final HabitRepository habitRepository;

  @Override
  public Optional<HabitTrack> save(HabitTrack habitTrack) {
    return Optional.of(habitTrackRepository.save(habitTrack));
  }

  @Override
  public Optional<Habit> findHabitTrackById(UUID id) {
    return Optional.empty();
  }

  @Override
  public void deleteById(UUID id) {
    habitTrackRepository.deleteById(id);
  }

  @Override
  public Page<HabitTrackResponse> getAllTracksOfHabit(UUID id) {
    var habit =
        habitRepository
            .findById(id)
            .orElseThrow(() -> new EntitySaveException(ExceptionMessages.entitySaveExceptionMessage));
    var listOfTracks = habit.getHabitTrackList();
    var listOfTracksResponses = listOfTracks.stream().map(this::mapToResponse).toList();

    Page<HabitTrackResponse> habitTrackResponsePage = new Page<>();
    habitTrackResponsePage.setItems(listOfTracksResponses);

    return habitTrackResponsePage;
  }

  @Override
  public HabitTrackResponse trackTheHabit(HabitTrackingRequest habitTrackingRequest) {

    var habitToUpdateTracking =
        habitRepository
            .findById(habitTrackingRequest.habitId())
            .orElseThrow(() -> new EntitySaveException(ExceptionMessages.entitySaveExceptionMessage));

    ZonedDateTime date = ZonedDateTime.now();
    String formattedDate = date.format(DateTimeFormatter.ISO_DATE_TIME);

    var track =
        HabitTrack.builder()
            .date(formattedDate)
            .status(habitTrackingRequest.status())
            .habit(habitToUpdateTracking)
            .build();

    var trackSavedToDB = save(track).orElseThrow(() ->new EntitySaveException(ExceptionMessages.entitySaveExceptionMessage));

    habitToUpdateTracking.getHabitTrackList().add(track);
    habitRepository
        .save(habitToUpdateTracking);

    return mapToResponse(trackSavedToDB);
  }

  private HabitTrackResponse mapToResponse(HabitTrack habitTrack) {
    return HabitTrackResponse.builder()
        .date(habitTrack.getDate())
        .status(habitTrack.getStatus())
        .build();
  }
}

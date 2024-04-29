package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.entity.Habit;
import com.dreamtracker.app.entity.HabitTrack;
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
        habitService
            .findHabitById(id)
            .orElseThrow(() -> new RuntimeException("Habit with provided id does not exist"));
    var listOfTracks = habit.getHabitTrackList();
    var listOfTracksResponses = listOfTracks.stream().map(this::mapToResponse).toList();

    Page<HabitTrackResponse> habitTrackResponsePage = new Page<>();
    habitTrackResponsePage.setItems(listOfTracksResponses);

    return habitTrackResponsePage;
  }

  @Override
  public HabitTrackResponse trackTheHabit(HabitTrackingRequest habitTrackingRequest) {

    var habitToUpdateTracking =
        habitService
            .findHabitById(habitTrackingRequest.habitId())
            .orElseThrow(() -> new RuntimeException("Tracking cannot be updated"));

    ZonedDateTime date = ZonedDateTime.now();
    String formattedDate = date.format(DateTimeFormatter.ISO_DATE_TIME);

    var track =
        HabitTrack.builder()
            .date(formattedDate)
            .status(habitTrackingRequest.status())
            .habit(habitToUpdateTracking)
            .build();

    var trackSavedToDB = save(track).orElseThrow(() -> new RuntimeException("Error saving track"));

    habitToUpdateTracking.getHabitTrackList().add(track);
    habitService
        .save(habitToUpdateTracking)
        .orElseThrow(() -> new RuntimeException("Error saving habit"));

    return mapToResponse(trackSavedToDB);
  }

  private HabitTrackResponse mapToResponse(HabitTrack habitTrack) {
    return HabitTrackResponse.builder()
        .date(habitTrack.getDate())
        .status(habitTrack.getStatus())
        .build();
  }
}

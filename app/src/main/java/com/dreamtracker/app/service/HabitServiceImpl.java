package com.dreamtracker.app.service;

import com.dreamtracker.app.entity.Habit;
import com.dreamtracker.app.entity.HabitTrack;
import com.dreamtracker.app.repository.HabitRepository;
import com.dreamtracker.app.request.HabitRequest;
import com.dreamtracker.app.request.HabitTrackingRequest;
import com.dreamtracker.app.response.HabitResponse;
import com.dreamtracker.app.utils.HabitStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

  private final HabitRepository habitRepository;
  private final HabitTrackService habitTrackService;

  @Override
  public Optional<Habit> save(Habit habit) {
    return Optional.of(habitRepository.save(habit));
  }

  @Override
  public Optional<Habit> findHabitById(UUID id) {
    return habitRepository.findById(id);
  }

  @Override
  @Transactional
  public void deleteById(UUID id) {
    habitRepository.deleteById(id);
  }

  @Override
  public Optional<HabitTrack> trackTheHabit(HabitTrackingRequest habitTrackingRequest) {
    ZonedDateTime date = ZonedDateTime.now();
    String formattedDate = date.format(DateTimeFormatter.ISO_DATE_TIME);
    var track =
        HabitTrack.builder().date(formattedDate).status(habitTrackingRequest.status()).build();

    return habitTrackService.save(track);
  }

  @Override
  public Optional<List<HabitTrack>> getHabitTrack(UUID id) {
    var habit =
        habitRepository.findById(id).orElseThrow(() -> new RuntimeException("Habit not found"));
    return Optional.of(habit.getHabitTrackList());
  }

  @Override
  public HabitResponse createHabit(HabitRequest habitRequest) {
    var habitToCreate =
        Habit.builder()
            .name(habitRequest.name())
            .action(habitRequest.action())
            .duration(habitRequest.duration())
            .difficulty(habitRequest.difficulty())
            .status(HabitStatus.ACTIVE.toString())
            .habitTrackList(new ArrayList<HabitTrack>())
            .build();

    var habitSavedToDB = save(habitToCreate).orElseThrow(() -> new RuntimeException("Error during saving habit"));

    return HabitResponse.builder()
            .id(habitSavedToDB.getId())
            .action(habitSavedToDB.getAction())
            .name(habitSavedToDB.getName())
            .duration(habitSavedToDB.getDuration())
            .difficulty(habitSavedToDB.getDifficulty())
            .status(habitSavedToDB.getStatus())
            .build();
  }
}

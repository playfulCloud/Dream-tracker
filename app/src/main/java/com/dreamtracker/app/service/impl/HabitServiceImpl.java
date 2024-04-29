package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.entity.Habit;
import com.dreamtracker.app.entity.HabitTrack;
import com.dreamtracker.app.repository.HabitRepository;
import com.dreamtracker.app.request.HabitCategoryCreateRequest;
import com.dreamtracker.app.request.HabitRequest;
import com.dreamtracker.app.request.HabitTrackingRequest;
import com.dreamtracker.app.response.HabitResponse;
import com.dreamtracker.app.response.Page;
import com.dreamtracker.app.security.CurrentUserProvider;
import com.dreamtracker.app.service.CategoryService;
import com.dreamtracker.app.service.HabitService;
import com.dreamtracker.app.service.HabitTrackService;
import com.dreamtracker.app.service.UserService;
import com.dreamtracker.app.utils.HabitStatus;
import jakarta.transaction.Transactional;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

  private final HabitRepository habitRepository;
  private final CurrentUserProvider currentUserProvider;
  private final UserService userService;
  private final CategoryService categoryService;

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
  @Transactional
  public boolean delete(UUID id) {
    if (existsById(id)) {
      deleteById(id);
      return true;
    }
    return false;
  }

  @Override
  public boolean existsById(UUID id) {
    return habitRepository.existsById(id);
  }

  @Override
  public Optional<List<HabitTrack>> getHabitTrack(UUID id) {
    var habit =
        habitRepository.findById(id).orElseThrow(() -> new RuntimeException("Habit not found"));
    return Optional.of(habit.getHabitTrackList());
  }

  @Override
  public HabitResponse createHabit(HabitRequest habitRequest) {

    var ownerOfHabit =
        userService
            .findById(currentUserProvider.getCurrentUser())
            .orElseThrow(() -> new RuntimeException("Error during finding owner of habit"));

    var habitToCreate =
        Habit.builder()
            .name(habitRequest.name())
            .action(habitRequest.action())
            .duration(habitRequest.duration())
            .difficulty(habitRequest.difficulty())
            .status(HabitStatus.ACTIVE.toString())
            .habitTrackList(new ArrayList<HabitTrack>())
            .categories(new ArrayList<>())
            .goals(new ArrayList<>())
            .user(ownerOfHabit)
            .build();

    var habitSavedToDB =
        save(habitToCreate).orElseThrow(() -> new RuntimeException("Error during saving habit"));

    ownerOfHabit.getHabits().add(habitSavedToDB);
    userService.save(ownerOfHabit);

    return mapToResponse(habitSavedToDB);
  }

  @Override
  public Page<HabitResponse> getAllUserHabits() {
    var ownerOfHabits =
        userService
            .findById(currentUserProvider.getCurrentUser())
            .orElseThrow(() -> new RuntimeException("Error during finding owner of habit"));
    var habits = ownerOfHabits.getHabits();

    var listOfHabitResponses = habits.stream().map(this::mapToResponse).toList();

    Page<HabitResponse> responsePage = new Page<>();
    responsePage.setItems(listOfHabitResponses);

    return responsePage;
  }

  @Override
  public HabitResponse updateHabit(UUID id, HabitRequest habitRequest) {
    var habitToUpdate =
        findHabitById(id).orElseThrow(() -> new RuntimeException("Request habit doesnt not exits"));

    Optional.ofNullable(habitRequest.name()).ifPresent(habitToUpdate::setName);
    Optional.ofNullable(habitRequest.action()).ifPresent(habitToUpdate::setAction);
    Optional.ofNullable(habitRequest.frequency()).ifPresent(habitToUpdate::setFrequency);
    Optional.ofNullable(habitRequest.duration()).ifPresent(habitToUpdate::setDuration);
    Optional.ofNullable(habitRequest.difficulty()).ifPresent(habitToUpdate::setDifficulty);

    var updatedHabit =
        save(habitToUpdate).orElseThrow(() -> new RuntimeException("Error updating habit "));

    return mapToResponse(updatedHabit);
  }

  @Override
  public void linkCategoryWithHabit(UUID habitId, HabitCategoryCreateRequest categoryCreateRequest) {
    var habitToLinkCategory = findHabitById(habitId).orElseThrow(() -> new RuntimeException("Later"));
    var categoryToBeLinked = categoryService.findById(categoryCreateRequest.id()).orElseThrow(() -> new RuntimeException("Category not found"));

    habitToLinkCategory.getCategories().add(categoryToBeLinked);
    categoryToBeLinked.getHabits().add(habitToLinkCategory);

    save(habitToLinkCategory).orElseThrow(() -> new RuntimeException("Error during saving habits"));
    categoryService.save(categoryToBeLinked).orElseThrow(() -> new RuntimeException("Error during saving habit"));
  }

  private HabitResponse mapToResponse(Habit habit) {
    return HabitResponse.builder()
        .id(habit.getId())
        .name(habit.getName())
        .action(habit.getName())
        .duration(habit.getDuration())
        .difficulty(habit.getDifficulty())
        .status(habit.getStatus())
        .build();
  }
}

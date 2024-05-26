package com.dreamtracker.app.habit.domain.ports;

import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.habit.domain.model.HabitTrack;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.habit.adapters.api.HabitCategoryCreateRequest;
import com.dreamtracker.app.habit.adapters.api.HabitRequest;
import com.dreamtracker.app.habit.adapters.api.HabitResponse;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.domain.ports.UserService;
import com.dreamtracker.app.habit.domain.utils.HabitStatus;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DomainHabitService implements HabitService {

  private final HabitRepositoryPort habitRepositoryPort;
  private final CurrentUserProvider currentUserProvider;
  private final UserService userService;
  private final CategoryRepositoryPort categoryRepositoryPort;
  private final HabitTrackRepositoryPort habitTrackRepositoryPort;

  @Override
  @Transactional
  public boolean delete(UUID id) {
    if (habitRepositoryPort.existsById(id)) {
      habitRepositoryPort.deleteById(id);
      return true;
    }
    return false;
  }

  @Override
  public List<HabitTrack> getHabitTrack(UUID id) {
    return habitTrackRepositoryPort.findByHabitUUID(id);
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
            .categories(new ArrayList<>())
            .goals(new ArrayList<>())
            .userUUID(currentUserProvider.getCurrentUser())
            .build();

    var habitSavedToDB = habitRepositoryPort.save(habitToCreate);

    return mapToResponse(habitSavedToDB);
  }

  @Override
  public Page<HabitResponse> getAllUserHabits() {
    var habits = habitRepositoryPort.findByUserUUID(currentUserProvider.getCurrentUser());
    var listOfHabitResponses = habits.stream().map(this::mapToResponse).toList();
    Page<HabitResponse> responsePage = new Page<>(listOfHabitResponses);
    return responsePage;
  }

  @Override
  public HabitResponse updateHabit(UUID id, HabitRequest habitRequest) {
    var habitToUpdate =
        habitRepositoryPort
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    Optional.ofNullable(habitRequest.name()).ifPresent(habitToUpdate::setName);
    Optional.ofNullable(habitRequest.action()).ifPresent(habitToUpdate::setAction);
    Optional.ofNullable(habitRequest.frequency()).ifPresent(habitToUpdate::setFrequency);
    Optional.ofNullable(habitRequest.duration()).ifPresent(habitToUpdate::setDuration);
    Optional.ofNullable(habitRequest.difficulty()).ifPresent(habitToUpdate::setDifficulty);

    var updatedHabit = habitRepositoryPort.save(habitToUpdate);

    return mapToResponse(updatedHabit);
  }

  @Override
  @Transactional
  public void linkCategoryWithHabit(
      UUID habitId, HabitCategoryCreateRequest categoryCreateRequest) {
    var habitToLinkCategory =
        habitRepositoryPort
            .findById(habitId)
            .orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    var categoryToBeLinked =
        categoryRepositoryPort
            .findById(categoryCreateRequest.id())
            .orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    habitToLinkCategory.getCategories().add(categoryToBeLinked);
    categoryToBeLinked.getHabits().add(habitToLinkCategory);

    habitRepositoryPort.save(habitToLinkCategory);
    categoryRepositoryPort.save(categoryToBeLinked);
  }

  @Override
  public HabitResponse getHabitById(UUID habitUUID) {
    var foundHabit = habitRepositoryPort.findById(habitUUID).orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    return mapToResponse(foundHabit);
  }

  private HabitResponse mapToResponse(Habit habit) {
    return HabitResponse.builder()
        .id(habit.getId())
        .name(habit.getName())
        .action(habit.getAction())
        .duration(habit.getDuration())
        .difficulty(habit.getDifficulty())
        .status(habit.getStatus())
        .build();
  }
}

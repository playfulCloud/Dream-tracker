package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.domain.habit.domain.Habit;
import com.dreamtracker.app.domain.entity.HabitTrack;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.domain.repository.CategoryRepository;
import com.dreamtracker.app.domain.repository.HabitRepository;
import com.dreamtracker.app.domain.repository.HabitTrackRepository;
import com.dreamtracker.app.domain.request.HabitCategoryCreateRequest;
import com.dreamtracker.app.domain.request.HabitRequest;
import com.dreamtracker.app.domain.response.HabitResponse;
import com.dreamtracker.app.domain.response.Page;
import com.dreamtracker.app.infrastructure.security.CurrentUserProvider;
import com.dreamtracker.app.service.HabitService;
import com.dreamtracker.app.service.UserService;
import com.dreamtracker.app.infrastructure.utils.HabitStatus;
import jakarta.transaction.Transactional;
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
  private final CategoryRepository categoryRepository;
  private final HabitTrackRepository habitTrackRepository;

  @Override
  @Transactional
  public boolean delete(UUID id) {
    if (habitRepository.existsById(id)) {
      habitRepository.deleteById(id);
      return true;
    }
    return false;
  }

  @Override
  public List<HabitTrack> getHabitTrack(UUID id) {
    return habitTrackRepository.findByHabitUUID(id);
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

    var habitSavedToDB = habitRepository.save(habitToCreate);

    return mapToResponse(habitSavedToDB);
  }

  @Override
  public Page<HabitResponse> getAllUserHabits() {
    var habits = habitRepository.findByUserUUID(currentUserProvider.getCurrentUser());
    var listOfHabitResponses = habits.stream().map(this::mapToResponse).toList();
    Page<HabitResponse> responsePage = new Page<>(listOfHabitResponses);
    return responsePage;
  }

  @Override
  public HabitResponse updateHabit(UUID id, HabitRequest habitRequest) {
    var habitToUpdate =
        habitRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    Optional.ofNullable(habitRequest.name()).ifPresent(habitToUpdate::setName);
    Optional.ofNullable(habitRequest.action()).ifPresent(habitToUpdate::setAction);
    Optional.ofNullable(habitRequest.frequency()).ifPresent(habitToUpdate::setFrequency);
    Optional.ofNullable(habitRequest.duration()).ifPresent(habitToUpdate::setDuration);
    Optional.ofNullable(habitRequest.difficulty()).ifPresent(habitToUpdate::setDifficulty);

    var updatedHabit = habitRepository.save(habitToUpdate);

    return mapToResponse(updatedHabit);
  }

  @Override
  @Transactional
  public void linkCategoryWithHabit(
      UUID habitId, HabitCategoryCreateRequest categoryCreateRequest) {
    var habitToLinkCategory =
        habitRepository
            .findById(habitId)
            .orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    var categoryToBeLinked =
        categoryRepository
            .findById(categoryCreateRequest.id())
            .orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    habitToLinkCategory.getCategories().add(categoryToBeLinked);
    categoryToBeLinked.getHabits().add(habitToLinkCategory);

    habitRepository.save(habitToLinkCategory);
    categoryRepository.save(categoryToBeLinked);
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

package com.dreamtracker.app.habit.adapters.api;

import com.dreamtracker.app.habit.domain.ports.HabitService;
import com.dreamtracker.app.habit.domain.ports.HabitTrackService;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class HabitController {


  private final HabitService habitService;
  private final HabitTrackService habitTrackService;

  @Qualifier("securityContextHolderUserProvider")
  private final CurrentUserProvider currentUserProvider;

  private static final Logger logger = LoggerFactory.getLogger(HabitController.class);

  @PostMapping("/habits")
  public ResponseEntity<HabitResponse> createHabit(@RequestBody HabitRequest habitRequest) {
    return new ResponseEntity<>(habitService.createHabit(habitRequest), HttpStatus.CREATED);
  }

  @GetMapping("/habits")
  public ResponseEntity<Page<HabitResponse>> getAllUserHabits() {
    return new ResponseEntity<>(habitService.getAllUserHabits(), HttpStatus.OK);
  }

  @PutMapping("/habits/{habit-id}")
  public ResponseEntity<HabitResponse> updateHabit(
      @PathVariable("habit-id") UUID id, @RequestBody HabitRequest habitRequest) {
    return new ResponseEntity<>(habitService.updateHabit(id, habitRequest), HttpStatus.OK);
  }

  @DeleteMapping("/habits/{habit-id}")
  public ResponseEntity<Void> deleteHabit(@PathVariable("habit-id") UUID id) {
    return habitService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @PostMapping("/habits-tracking")
  public ResponseEntity<HabitTrackResponse> trackTheHabit(
      @RequestBody HabitTrackingRequest habitTrackingRequest) {
    return new ResponseEntity<>(
        habitTrackService.trackTheHabit(habitTrackingRequest), HttpStatus.CREATED);
  }

  @GetMapping("/habits-tracking/{habit-id}")
  public ResponseEntity<Page<HabitTrackResponse>> getAllHabitTracks(
      @PathVariable("habit-id") UUID id) {
    return new ResponseEntity<>(habitTrackService.getAllTracksOfHabit(id), HttpStatus.OK);
  }

  @PostMapping("/habits/{habit-id}/categories")
  public ResponseEntity<Void> linkHabitWithCategory(@PathVariable("habit-id") UUID id,@RequestBody HabitCategoryCreateRequest habitCategoryCreateRequest){
//    logger.trace(id + " " + habitCategoryCreateRequest);
    habitService.linkCategoryWithHabit(id,habitCategoryCreateRequest);
    return ResponseEntity.noContent().build();
  }
   @GetMapping("/habits/{habit-id}")
  public ResponseEntity<HabitResponse>getHabitById(@PathVariable("habit-id") UUID id){
      return new ResponseEntity<>(habitService.getHabitById(id),HttpStatus.OK);
   }
}

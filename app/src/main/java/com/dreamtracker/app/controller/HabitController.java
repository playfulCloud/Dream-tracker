package com.dreamtracker.app.controller;

import com.dreamtracker.app.request.HabitRequest;
import com.dreamtracker.app.response.HabitResponse;
import com.dreamtracker.app.response.Page;
import com.dreamtracker.app.service.HabitService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class HabitController {

  private final HabitService habitService;

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
}

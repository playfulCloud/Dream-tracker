package com.dreamtracker.app.goal.adapters.api;

import com.dreamtracker.app.goal.domain.ports.GoalService;
import com.dreamtracker.app.habit.adapters.api.GoalAssignHabitRequest;
import com.dreamtracker.app.infrastructure.response.Page;
import java.util.UUID;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@Data
public class GoalController {

  private final GoalService goalService;

  @PostMapping("/goals")
  public ResponseEntity<GoalResponse> createGoal(@RequestBody GoalRequest goalRequest) {
    return new ResponseEntity<>(goalService.createGoal(goalRequest), HttpStatus.CREATED);
  }

  @GetMapping("/goals")
  public ResponseEntity<Page<GoalResponse>> getAllUserGoals() {
    return new ResponseEntity<>(goalService.getAllUserGoals(), HttpStatus.OK);
  }

  @PutMapping("/goals/{goal-id}")
  public ResponseEntity<GoalResponse> updateGoal(
      @PathVariable("goal-id") UUID id, @RequestBody GoalRequest goalRequest) {
    return new ResponseEntity<>(goalService.updateGoal(id, goalRequest), HttpStatus.OK);
  }

  @DeleteMapping("/goals/{goal-id}")
  public ResponseEntity<Void> deleteGoal(@PathVariable("goal-id") UUID id) {
    return goalService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/goals/{goal-id}")
  public ResponseEntity<GoalResponse> getGoalById(@PathVariable("goal-id") UUID id) {
    return new ResponseEntity<>(goalService.getGoalById(id), HttpStatus.OK);
  }


}

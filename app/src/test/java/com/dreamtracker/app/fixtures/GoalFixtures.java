package com.dreamtracker.app.fixtures;

import com.dreamtracker.app.goal.adapters.api.GoalRequest;
import com.dreamtracker.app.goal.adapters.api.GoalResponse;
import com.dreamtracker.app.goal.domain.model.Goal;
import java.util.ArrayList;
import java.util.UUID;

public interface GoalFixtures {

  default Goal.GoalBuilder getSampleGoalBuilder(UUID userUUID) {
    return Goal.builder()
        .uuid(UUID.fromString("7b8696f7-dc0e-4741-8faf-b09d3fd71bef"))
        .name("Running 5k")
        .userUUID(userUUID)
        .duration("P30M")
        .habitList(new ArrayList<>());
  }

  default GoalRequest.GoalRequestBuilder getSampleGoalRequestBuilder() {
    return GoalRequest.builder().name("Running 5k").duration("P30M");
  }

  default GoalResponse.GoalResponseBuilder getExpectedGoalResponse() {
    return GoalResponse.builder()
        .id(UUID.fromString("7b8696f7-dc0e-4741-8faf-b09d3fd71bef"))
        .name("Running 5k")
        .duration("P30M");
  }

  default Goal.GoalBuilder getSampleGoalForPageBuilder(UUID userUUID) {
    return Goal.builder()
        .uuid(UUID.fromString("336d8a9a-2464-41f2-8a8f-2d37a78c88ae"))
        .name("Running 25k")
        .userUUID(userUUID)
        .duration("P30M")
        .habitList(new ArrayList<>());
  }

  default Goal.GoalBuilder getSampleUpdatedGoalBuilder(UUID userUUID) {
    return Goal.builder()
        .uuid(UUID.fromString("7b8696f7-dc0e-4741-8faf-b09d3fd71bef"))
        .name("Walking 1k")
        .userUUID(userUUID)
        .duration("P30M")
        .habitList(new ArrayList<>());
  }

  default GoalRequest.GoalRequestBuilder getSampleUpdateGoalRequestBuilder() {
    return GoalRequest.builder().name("Walking 1k").duration("P30M");
  }

  default GoalResponse.GoalResponseBuilder getUpdatedExpectedGoalResponse() {
    return GoalResponse.builder()
        .id(UUID.fromString("7b8696f7-dc0e-4741-8faf-b09d3fd71bef"))
        .name("Walking 1k")
        .duration("P30M");
  }
}

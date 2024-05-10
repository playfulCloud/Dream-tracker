package com.dreamtracker.app.fixtures;

import com.dreamtracker.app.entity.Goal;
import com.dreamtracker.app.entity.User;
import com.dreamtracker.app.request.GoalRequest;
import com.dreamtracker.app.response.GoalResponse;

import java.util.ArrayList;
import java.util.UUID;

public interface GoalFixtures {

  default Goal.GoalBuilder getSampleGoalBuilder(User user) {
    return Goal.builder()
            .uuid(UUID.fromString("7b8696f7-dc0e-4741-8faf-b09d3fd71bef"))
        .name("Running 5k")
        .user(user)
        .duration("P30M")
        .habitList(new ArrayList<>()
        );
  }

  default GoalRequest.GoalRequestBuilder getSampleGoalRequestBuilder() {
        return GoalRequest.builder()
                .name("Running 5k")
                .duration("P30M");
  }

  default GoalResponse.GoalResponseBuilder getExpectedGoalResponse() {
     return GoalResponse.builder()
             .id(UUID.fromString("7b8696f7-dc0e-4741-8faf-b09d3fd71bef"))
             .name("Running 5k")
             .duration("P30M");
  }
//
//  default Goal.GoalBuilder getSampleGoalForPageBuilder(){
//
//  }

}

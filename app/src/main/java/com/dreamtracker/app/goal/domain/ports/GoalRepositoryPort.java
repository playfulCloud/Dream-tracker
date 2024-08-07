package com.dreamtracker.app.goal.domain.ports;

import com.dreamtracker.app.goal.adapters.api.GoalResponse;
import com.dreamtracker.app.goal.domain.model.Goal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GoalRepositoryPort {
   Goal save(Goal goal);
   Boolean existsById(UUID id);
   void deleteById(UUID id);
   Optional<Goal>findById(UUID id);
   List<Goal>findByUserUUID(UUID userUUID);
}
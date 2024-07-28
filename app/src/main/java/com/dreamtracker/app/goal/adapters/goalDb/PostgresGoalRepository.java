package com.dreamtracker.app.goal.adapters.goalDb;

import com.dreamtracker.app.goal.adapters.api.GoalResponse;
import com.dreamtracker.app.goal.domain.model.Goal;
import com.dreamtracker.app.goal.domain.ports.GoalRepositoryPort;
import com.dreamtracker.app.infrastructure.repository.SpringDataGoalRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostgresGoalRepository implements GoalRepositoryPort {

    private final SpringDataGoalRepository springDataGoalRepository;
    @Override
    public Goal save(Goal goal) {
        return springDataGoalRepository.save(goal);
    }

    @Override
    public Boolean existsById(UUID id) {
        return springDataGoalRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        springDataGoalRepository.deleteById(id);
    }

    @Override
    public Optional<Goal> findById(UUID id) {
        return springDataGoalRepository.findById(id);
    }

    @Override
    public List<Goal> findByUserUUID(UUID userUUID) {
        return springDataGoalRepository.findByUserUUID(userUUID);
    }


}

package com.dreamtracker.app.view.adapters.aggregatesDB;

import com.dreamtracker.app.goal.domain.model.Goal;
import com.dreamtracker.app.infrastructure.repository.SpringDataBreaksAggregateRepository;
import com.dreamtracker.app.infrastructure.repository.SpringDataGoalRepository;
import com.dreamtracker.app.view.domain.model.BreaksAggregate;
import com.dreamtracker.app.view.domain.ports.BreaksAggregateRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class PostgresBreaksAggregateRepository implements BreaksAggregateRepositoryPort {

    private final SpringDataBreaksAggregateRepository springDataBreaksAggregateRepository;

    @Override
    public BreaksAggregate save(BreaksAggregate breaksAggregate) {
        return springDataBreaksAggregateRepository.save(breaksAggregate);
    }

    @Override
    public Boolean existsById(UUID id) {
        return springDataBreaksAggregateRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        springDataBreaksAggregateRepository.deleteById(id);
    }

    @Override
    public Optional<BreaksAggregate> findById(UUID id) {
        return springDataBreaksAggregateRepository.findById(id);
    }

    @Override
    public List<BreaksAggregate> findByHabitUUID(UUID habitUUID) {
        return springDataBreaksAggregateRepository.findByHabitUUID(habitUUID);
    }
}

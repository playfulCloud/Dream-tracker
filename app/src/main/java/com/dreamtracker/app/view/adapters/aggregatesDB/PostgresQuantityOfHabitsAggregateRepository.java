package com.dreamtracker.app.view.adapters.aggregatesDB;

import com.dreamtracker.app.infrastructure.repository.SpringDataBreaksAggregateRepository;
import com.dreamtracker.app.view.domain.model.BreaksAggregate;
import com.dreamtracker.app.view.domain.ports.QuantityOfHabitsAggregateRepositoryPort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PostgresQuantityOfHabitsAggregateRepository implements QuantityOfHabitsAggregateRepositoryPort {

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

package com.dreamtracker.app.view.adapters.aggregatesDB;

import com.dreamtracker.app.infrastructure.repository.SpringDataStreakAggregateRepository;
import com.dreamtracker.app.view.domain.model.aggregate.StreakAggregate;
import com.dreamtracker.app.view.domain.ports.StreakAggregateRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostgresStreakAggregateRepository implements StreakAggregateRepositoryPort {

  private final SpringDataStreakAggregateRepository springDataStreakAggregateRepository;

  @Override
  public StreakAggregate save(StreakAggregate streakAggregate) {
    return springDataStreakAggregateRepository.save(streakAggregate);
    }

    @Override
    public Boolean existsById(UUID id) {
    return springDataStreakAggregateRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
    springDataStreakAggregateRepository.deleteById(id);
    }

  @Override
  public Optional<StreakAggregate> findById(UUID id) {
    return springDataStreakAggregateRepository.findById(id);
    }

  @Override
  public List<StreakAggregate> findByHabitUUID(UUID habitUUID) {
    return springDataStreakAggregateRepository.findByHabitUUID(habitUUID);
    }
}

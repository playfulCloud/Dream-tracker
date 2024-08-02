package com.dreamtracker.app.view.adapters.aggregatesDB;

import com.dreamtracker.app.infrastructure.repository.SpringDataQuantityOfHabitsAggregateRepository;
import com.dreamtracker.app.view.domain.model.aggregate.QuantityOfHabitsAggregate;
import com.dreamtracker.app.view.domain.ports.QuantityOfHabitsAggregateRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostgresQuantityOfHabitsAggregateRepository
    implements QuantityOfHabitsAggregateRepositoryPort {

  private final SpringDataQuantityOfHabitsAggregateRepository
      springDataQuantityOfHabitsAggregateRepository;

  @Override
  public QuantityOfHabitsAggregate save(QuantityOfHabitsAggregate quantityOfHabitsAggregate) {
    return springDataQuantityOfHabitsAggregateRepository.save(quantityOfHabitsAggregate);
    }

    @Override
    public Boolean existsById(UUID id) {
    return springDataQuantityOfHabitsAggregateRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
    springDataQuantityOfHabitsAggregateRepository.deleteById(id);
    }

  @Override
  public Optional<QuantityOfHabitsAggregate> findById(UUID id) {
    return springDataQuantityOfHabitsAggregateRepository.findById(id);
    }

  @Override
  public Optional<QuantityOfHabitsAggregate> findByHabitUUID(UUID habitUUID) {
    return springDataQuantityOfHabitsAggregateRepository.findByHabitUUID(habitUUID);
    }
}

package com.dreamtracker.app.view.adapters.aggregatesDB;

import com.dreamtracker.app.infrastructure.repository.SpringDataDependingOnDayAggregateRepository;
import com.dreamtracker.app.view.domain.model.aggregate.DependingOnDayAggregate;
import com.dreamtracker.app.view.domain.ports.DependingOnDayRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostgresDependingOnDayAggregateRepository implements DependingOnDayRepositoryPort {

  private final SpringDataDependingOnDayAggregateRepository
      springDataDependingOnDayAggregateRepository;

  @Override
  public DependingOnDayAggregate save(DependingOnDayAggregate breaksAggregate) {
    return springDataDependingOnDayAggregateRepository.save(breaksAggregate);
    }

    @Override
    public Boolean existsById(UUID id) {
    return springDataDependingOnDayAggregateRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
    springDataDependingOnDayAggregateRepository.deleteById(id);
    }

  @Override
  public Optional<DependingOnDayAggregate> findById(UUID id) {
    return springDataDependingOnDayAggregateRepository.findById(id);
    }

  @Override
  public Optional<DependingOnDayAggregate> findByHabitUUID(UUID habitUUID) {
    return springDataDependingOnDayAggregateRepository.findByHabitUUID(habitUUID);
    }
}

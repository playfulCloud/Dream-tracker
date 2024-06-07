package com.dreamtracker.app.view.adapters.aggregatesDB;

import com.dreamtracker.app.infrastructure.repository.SpringDataSingleDayAggregateRepository;
import com.dreamtracker.app.view.domain.model.aggregate.SingleDayAggregate;
import com.dreamtracker.app.view.domain.ports.SingleDayAggregateRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostgresSingleDayRepository implements SingleDayAggregateRepositoryPort {

  private final SpringDataSingleDayAggregateRepository springDataSingleDayAggregateRepository;

  @Override
  public SingleDayAggregate save(SingleDayAggregate singleDayAggregate) {
    return springDataSingleDayAggregateRepository.save(singleDayAggregate);
    }

    @Override
    public Boolean existsById(UUID id) {
    return springDataSingleDayAggregateRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
    springDataSingleDayAggregateRepository.deleteById(id);
    }

  @Override
  public Optional<SingleDayAggregate> findById(UUID id) {
    return springDataSingleDayAggregateRepository.findById(id);
    }

  @Override
  public List<SingleDayAggregate> findByHabitUUID(UUID habitUUID) {
    return springDataSingleDayAggregateRepository.findByHabitUUID(habitUUID);
    }
}

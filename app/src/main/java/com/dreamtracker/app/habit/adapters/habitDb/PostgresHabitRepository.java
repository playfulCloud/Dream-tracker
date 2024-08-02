package com.dreamtracker.app.habit.adapters.habitDb;

import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.habit.domain.ports.HabitRepositoryPort;
import com.dreamtracker.app.infrastructure.repository.SpringDataHabitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Component
public class PostgresHabitRepository implements HabitRepositoryPort {

   private final SpringDataHabitRepository springDataHabitRepository;


    @Override
    public Boolean existsById(UUID id) {
        return springDataHabitRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        springDataHabitRepository.deleteById(id);
    }

    @Override
    public List<Habit> findByUserUUID(UUID userUUID) {
        return springDataHabitRepository.findByUserUUID(userUUID);
    }

    @Override
    public Optional<Habit> findById(UUID id) {
        return springDataHabitRepository.findById(id);
    }

    @Override
    public Habit save(Habit habit) {
        return springDataHabitRepository.save(habit);
    }

    @Override
    public List<Habit> findAll() {
        return springDataHabitRepository.findAll();
    }
}

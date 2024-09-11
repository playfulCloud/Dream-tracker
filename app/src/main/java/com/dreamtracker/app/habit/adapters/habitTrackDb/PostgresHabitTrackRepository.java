package com.dreamtracker.app.habit.adapters.habitTrackDb;

import com.dreamtracker.app.habit.domain.model.HabitTrack;
import com.dreamtracker.app.habit.domain.ports.HabitTrackRepositoryPort;
import com.dreamtracker.app.infrastructure.repository.SpringDataHabitTrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class PostgresHabitTrackRepository implements HabitTrackRepositoryPort {

    private final SpringDataHabitTrackRepository springHabitTrackRepository;

    @Override
    public Optional<HabitTrack> findById(UUID id) {
        return springHabitTrackRepository.findById(id);
    }

    @Override
    public List<HabitTrack> findByHabitUUID(UUID habitUUID) {
        return springHabitTrackRepository.findByHabitUUID(habitUUID);
    }

    @Override
    public HabitTrack save(HabitTrack habitTrack) {
        return springHabitTrackRepository.save(habitTrack);
    }


    @Override
    public List<HabitTrack> findAllByUserUUID(UUID userUUID) {
        return springHabitTrackRepository.findAllByUserUUID(userUUID);
    }
}

package com.dreamtracker.app.service.impl;


import com.dreamtracker.app.entity.Habit;
import com.dreamtracker.app.entity.HabitTrack;
import com.dreamtracker.app.repository.HabitTrackRepository;
import com.dreamtracker.app.service.HabitTrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HabitTrackServiceImpl implements HabitTrackService {

    private final HabitTrackRepository habitTrackRepository;


    @Override
    public Optional<HabitTrack> save(HabitTrack habitTrack) {
        return Optional.of(habitTrackRepository.save(habitTrack));
    }

    @Override
    public Optional<Habit> findHabitTrackById(UUID id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(UUID id) {
        habitTrackRepository.deleteById(id);
    }
}

package com.dreamtracker.service;

import com.dreamtracker.entity.Habit;
import com.dreamtracker.entity.HabitTrack;
import com.dreamtracker.repository.HabitRepository;
import com.dreamtracker.repository.HabitTrackRepository;
import com.dreamtracker.request.HabitTrackingRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {


    private final HabitRepository habitRepository;
    private final HabitTrackService habitTrackService;

    @Override
    public Optional<Habit> save(Habit habit) {
        return Optional.of(habitRepository.save(habit));
    }

    @Override
    public Optional<Habit> findHabitById(UUID id) {
        return habitRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        habitRepository.deleteById(id);
    }

    @Override
    public Optional<HabitTrack> trackTheHabit(HabitTrackingRequest habitTrackingRequest) {
        ZonedDateTime date = ZonedDateTime.now();
        String formattedDate = date.format(DateTimeFormatter.ISO_DATE_TIME);
        var track = HabitTrack.builder()
                .date(formattedDate)
                .status(habitTrackingRequest.status())
                .build();

        return habitTrackService.save(track);
    }

    @Override
    public Optional<List<HabitTrack>> getHabitTrack(UUID id) {
        var habit = habitRepository.findById(id).orElseThrow(() -> new RuntimeException("Habit not found"));
        return Optional.of(habit.getHabitTrackList());
    }
}

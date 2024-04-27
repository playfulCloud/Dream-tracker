package com.dreamtracker.repository;

import com.dreamtracker.entity.HabitTrack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HabitTrackRepository extends JpaRepository<HabitTrack, UUID> {
}

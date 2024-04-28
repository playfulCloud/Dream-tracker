package com.dreamtracker.repository;

import com.dreamtracker.entity.HabitTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HabitTrackRepository extends JpaRepository<HabitTrack, UUID> {
}

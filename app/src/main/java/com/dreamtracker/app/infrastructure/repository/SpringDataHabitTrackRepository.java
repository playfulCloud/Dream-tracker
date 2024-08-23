package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.habit.domain.model.HabitTrack;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataHabitTrackRepository extends JpaRepository<HabitTrack, UUID> {
    List<HabitTrack> findByHabitUUID(UUID id);
    @Query("SELECT ht FROM HabitTrack ht WHERE ht.habitUUID IN " +
            "(SELECT h.id FROM Habit h WHERE h.userUUID = :userUUID)")
    List<HabitTrack> findAllByUserUUID(@Param("userUUID") UUID userUUID);
}

package com.dreamtracker.repository;

import com.dreamtracker.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HabitRepository extends JpaRepository<Habit, UUID> {
}

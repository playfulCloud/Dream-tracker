package com.dreamtracker.app.view.domain.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class StreakAggregate {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID habitUUID;
    private int longestStreak;
    private int currentStreak;
}

package com.dreamtracker.app.habit.domain.model;


import com.dreamtracker.app.goal.domain.model.Goal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjuster;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Habits")
public class Habit {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String action;
    private String frequency;
    private String duration;
    private String difficulty;
    private String status;
    private UUID userUUID;
    private Instant coolDownTill;

    @Version
    private Integer version;

    @ManyToMany
    @JoinTable(
            name = "habit_category",
            joinColumns = @JoinColumn(name = "habit_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category>categories;

    @ManyToMany
    @JoinTable(
            name = "habit_goal",
            joinColumns = @JoinColumn(name = "habit_id"),
            inverseJoinColumns = @JoinColumn(name = "goal_id")
    )
    private List<Goal>goals;


}

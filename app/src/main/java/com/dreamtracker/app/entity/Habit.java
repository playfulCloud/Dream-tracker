package com.dreamtracker.app.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Habit {

    @Id
    @GeneratedValue
    private  UUID id;
    private String name;
    private String action;
    private String frequency;
    private String duration;
    private String difficulty;
    private String status;
    private UUID userUUID;

    @OneToMany(mappedBy = "habit", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH }, orphanRemoval = true)
    private List<HabitTrack>habitTrackList;


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

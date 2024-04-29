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
    private String duration;
    private String difficulty;
    private String status;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HabitTrack>habitTrackList;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

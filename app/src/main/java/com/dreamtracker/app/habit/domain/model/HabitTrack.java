package com.dreamtracker.app.habit.domain.model;


import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "HabitTracks")
public class HabitTrack {

    @Id
    @GeneratedValue
    private UUID id;
  private Instant date;
    private String status;
    private UUID habitUUID;
}

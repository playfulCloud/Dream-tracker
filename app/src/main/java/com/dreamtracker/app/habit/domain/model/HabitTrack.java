package com.dreamtracker.app.habit.domain.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HabitTrack {

    @Id
    @GeneratedValue
    private UUID id;
    private OffsetDateTime date;
    private String status;
    private UUID habitUUID;
}

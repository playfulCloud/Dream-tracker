package com.dreamtracker.app.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String date;
    private String status;
    private UUID habitUUID;
}

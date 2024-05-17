package com.dreamtracker.app.domain.entity;
import com.dreamtracker.app.domain.habit.domain.Habit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goal {

    @Id
    @GeneratedValue
    private UUID uuid;
    private String name;
    private String duration;
    private UUID userUUID;

    @ManyToMany(mappedBy = "goals")
    private List<Habit> habitList;


}

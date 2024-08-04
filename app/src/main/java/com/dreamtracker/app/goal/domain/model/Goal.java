package com.dreamtracker.app.goal.domain.model;
import com.dreamtracker.app.habit.domain.model.Habit;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String status;
    private UUID userUUID;
    private UUID habitUUID;
    private int completionCount;
    private int currentCount;
    private Instant createdAt;


    public void increaseCompletionCount(){
        this.currentCount +=1;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Goal goal)) return false;
        return completionCount == goal.completionCount && currentCount == goal.currentCount && Objects.equals(uuid, goal.uuid) && Objects.equals(name, goal.name) && Objects.equals(duration, goal.duration) && Objects.equals(status, goal.status) && Objects.equals(userUUID, goal.userUUID) && Objects.equals(habitUUID, goal.habitUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, duration, status, userUUID, habitUUID, completionCount, currentCount);
    }
}

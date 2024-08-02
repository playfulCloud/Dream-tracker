package com.dreamtracker.app.view.domain.model.aggregate;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StreakAggregate {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID habitUUID;
    private int longestStreak;
    private int currentStreak;

    public void increaseCurrentStreak(){
       var current = getCurrentStreak();
       if(current==getLongestStreak()){
          setLongestStreak(current+1);
       }
       setCurrentStreak(current+1);
    }

    @Override
    public String toString() {
        return "StreakAggregate{" +
                "id=" + id +
                ", habitUUID=" + habitUUID +
                ", longestStreak=" + longestStreak +
                ", currentStreak=" + currentStreak +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StreakAggregate that)) return false;
        return longestStreak == that.longestStreak && currentStreak == that.currentStreak && Objects.equals(id, that.id) && Objects.equals(habitUUID, that.habitUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, habitUUID, longestStreak, currentStreak);
    }
}

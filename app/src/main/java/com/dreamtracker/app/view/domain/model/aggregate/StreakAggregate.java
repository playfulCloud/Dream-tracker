package com.dreamtracker.app.view.domain.model.aggregate;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

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
}

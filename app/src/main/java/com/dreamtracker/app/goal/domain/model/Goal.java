package com.dreamtracker.app.goal.domain.model;
import com.dreamtracker.app.habit.domain.model.Habit;
import jakarta.persistence.*;
import java.util.List;
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
    private UUID userUUID;
    private UUID habitUUID;
    private int completionCount;
    private int currentCount;


    public void increaseCompletionCount(){
        this.currentCount +=1;
    }



}

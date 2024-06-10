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
public class QuantityOfHabitsAggregate {
    @Id
    @GeneratedValue
    private UUID id;
    private  UUID habitUUID;
    private int doneHabits;
    private int unDoneHabits;
    private int currentTrend;


    public void increaseDoneHabitsCount(){
        var doneHabits = getDoneHabits();
        var currentTrendValue= getCurrentTrend();
        setDoneHabits(doneHabits+1);
        setCurrentTrend(currentTrendValue+1);
    }

    public void increaseUnDoneHabitsCount(){
        var undoneHabits = getUnDoneHabits();
        var  currentTrendValue = getCurrentTrend();
        setUnDoneHabits(undoneHabits+1);
        setCurrentTrend(currentTrendValue-1);
    }

    @Override
    public String toString() {
        return "QuantityOfHabitsAggregate{" +
                "id=" + id +
                ", habitUUID=" + habitUUID +
                ", doneHabits=" + doneHabits +
                ", unDoneHabits=" + unDoneHabits +
                ", currentTrend=" + currentTrend +
                '}';
    }
}

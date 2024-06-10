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
public class SingleDayAggregate {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID habitUUID;

    private int mostDone;
    private int actualCount;
    private String date;


    public void increaseActualCount(){
       var current = getActualCount();
       setActualCount(current+1);
       if (getMostDone() == current){
          setMostDone(current+1);
       }
    }


    @Override
    public String toString() {
        return "SingleDayAggregate{" +
                "id=" + id +
                ", habitUUID=" + habitUUID +
                ", mostDone=" + mostDone +
                ", actualCount=" + actualCount +
                ", date='" + date + '\'' +
                '}';
    }
}

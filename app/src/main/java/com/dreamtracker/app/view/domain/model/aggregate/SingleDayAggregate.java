package com.dreamtracker.app.view.domain.model.aggregate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;
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
    private Instant date;


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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleDayAggregate that = (SingleDayAggregate) o;
        return mostDone == that.mostDone && actualCount == that.actualCount && Objects.equals(id, that.id) && Objects.equals(habitUUID, that.habitUUID) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, habitUUID, mostDone, actualCount, date);
    }
}

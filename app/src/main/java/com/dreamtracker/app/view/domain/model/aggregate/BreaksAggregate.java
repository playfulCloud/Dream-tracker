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
public class BreaksAggregate {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID habitUUID;
    private int sumOfBreaks;
    private int breaksQuantity;
    private boolean isBreak;

//    @Override
//    public String toString() {
//        return "BreaksAggregate{" +
//                "id=" + id +
//                ", habitUUID=" + habitUUID +
//                ", sumOfBreaks=" + sumOfBreaks +
//                ", breaksQuantity=" + breaksQuantity +
//                ", isBreak=" + isBreak +
//                '}';
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BreaksAggregate that = (BreaksAggregate) o;
        return sumOfBreaks == that.sumOfBreaks &&
                breaksQuantity == that.breaksQuantity &&
                isBreak == that.isBreak &&
                Objects.equals(id, that.id) &&
                Objects.equals(habitUUID, that.habitUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, habitUUID, sumOfBreaks, breaksQuantity, isBreak);
    }

}

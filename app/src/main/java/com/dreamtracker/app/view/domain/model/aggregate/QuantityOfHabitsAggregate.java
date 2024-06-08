package com.dreamtracker.app.view.domain.model.aggregate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuantityOfHabitsAggregate {
    @Id
    @GeneratedValue
    private UUID id;
    private  UUID habitUUID;
    private int doneHabits;
    private int unDoneHabits;
    private int doneInRow;
    private int unDoneInRow;
}

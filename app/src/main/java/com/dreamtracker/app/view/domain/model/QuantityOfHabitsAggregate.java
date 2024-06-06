package com.dreamtracker.app.view.domain.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

public class QuantityOfHabitsAggregate {

    @Id
    @GeneratedValue
    private UUID id;

    private  UUID habitUUID;
    private int DoneHabits;
    private int UnDoneHabits;
}

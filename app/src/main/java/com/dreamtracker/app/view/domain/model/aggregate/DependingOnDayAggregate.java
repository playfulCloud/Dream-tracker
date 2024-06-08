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
public class DependingOnDayAggregate {
    @Id
    @GeneratedValue
    private UUID id;

    private  UUID habitUUID;
    private int mondayDoneCount;
    private int mondayUnDoneCount;

    private int tuesdayDoneCount;
    private int tuesdayUnDoneCount;

    private int wednesdayDoneCount;
    private int wednesdayUnDoneCount;

    private int thursdayDoneCount;
    private int thursdayUnDoneCount;

    private int fridayDoneCount;
    private int fridayUnDoneCount;


    private int saturdayDoneCount;
    private int saturdayUnDoneCount;

    private int sundayDoneCount;
    private int sundayUnDoneCount;

}


package com.dreamtracker.app.view.domain.model.aggregate;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DependingOnDayAggregate {
    @Id
    @GeneratedValue
    private UUID id;

    private  UUID habitUUID;
    private int MondayDoneCount;
    private int MondayUnDoneCount;

    private int TuesdayDoneCount;
    private int TuesdayUnDoneCount;

    private int WednesdayDoneCount;
    private int WednesdayUnDoneCount;

    private int ThursdayDoneCount;
    private int ThursdayUnDoneCount;

    private int FridayDoneCount;
    private int FridayUnDoneCount;


    private int SaturdayDoneCount;
    private int SaturdayUnDoneCount;

    private int SundayDoneCount;
    private int SundayUnDoneCount;

}


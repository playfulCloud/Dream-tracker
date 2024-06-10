package com.dreamtracker.app.view.domain.model.aggregate;


import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
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



    public void increaseDoneCountBasedOnDayOfWeek(DayOfWeek dayOfWeek){
        switch (dayOfWeek){
            case MONDAY-> setMondayDoneCount(getMondayDoneCount()+1);
            case TUESDAY -> setTuesdayDoneCount(getTuesdayDoneCount()+1);
            case WEDNESDAY -> setWednesdayDoneCount(getWednesdayDoneCount()+1);
            case THURSDAY -> setThursdayDoneCount(getThursdayDoneCount()+1);
            case FRIDAY -> setFridayDoneCount(getFridayDoneCount()+1);
            case SATURDAY ->setSaturdayDoneCount(getSaturdayDoneCount()+1);
            case SUNDAY -> setSundayDoneCount(getSundayDoneCount()+1);
        }
    }

    public void increaseUnDoneCountBasedOnDayOfWeek(DayOfWeek dayOfWeek){
        switch (dayOfWeek){
            case MONDAY -> setMondayUnDoneCount(getMondayUnDoneCount() + 1);
            case TUESDAY -> setTuesdayUnDoneCount(getTuesdayUnDoneCount() + 1);
            case WEDNESDAY-> setWednesdayUnDoneCount(getWednesdayUnDoneCount() + 1);
            case THURSDAY -> setThursdayUnDoneCount(getThursdayUnDoneCount() + 1);
            case FRIDAY -> setFridayUnDoneCount(getFridayUnDoneCount() + 1);
            case SATURDAY -> setSaturdayUnDoneCount(getSaturdayUnDoneCount() + 1);
            case SUNDAY -> setSundayUnDoneCount(getSundayUnDoneCount() + 1);
        }
    }


}


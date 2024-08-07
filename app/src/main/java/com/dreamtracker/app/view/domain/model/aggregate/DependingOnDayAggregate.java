package com.dreamtracker.app.view.domain.model.aggregate;


import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Objects;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DependingOnDayAggregate that = (DependingOnDayAggregate) o;
        return mondayDoneCount == that.mondayDoneCount &&
                mondayUnDoneCount == that.mondayUnDoneCount &&
                tuesdayDoneCount == that.tuesdayDoneCount &&
                tuesdayUnDoneCount == that.tuesdayUnDoneCount &&
                wednesdayDoneCount == that.wednesdayDoneCount &&
                wednesdayUnDoneCount == that.wednesdayUnDoneCount &&
                thursdayDoneCount == that.thursdayDoneCount &&
                thursdayUnDoneCount == that.thursdayUnDoneCount &&
                fridayDoneCount == that.fridayDoneCount &&
                fridayUnDoneCount == that.fridayUnDoneCount &&
                saturdayDoneCount == that.saturdayDoneCount &&
                saturdayUnDoneCount == that.saturdayUnDoneCount &&
                sundayDoneCount == that.sundayDoneCount &&
                sundayUnDoneCount == that.sundayUnDoneCount &&
                Objects.equals(id, that.id) &&
                Objects.equals(habitUUID, that.habitUUID);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, habitUUID, mondayDoneCount, mondayUnDoneCount, tuesdayDoneCount, tuesdayUnDoneCount, wednesdayDoneCount, wednesdayUnDoneCount, thursdayDoneCount, thursdayUnDoneCount, fridayDoneCount, fridayUnDoneCount, saturdayDoneCount, saturdayUnDoneCount, sundayDoneCount, sundayUnDoneCount);
    }

    @Override
    public String toString() {
        return "DependingOnDayAggregate{" +
                "id=" + id +
                ", habitUUID=" + habitUUID +
                ", mondayDoneCount=" + mondayDoneCount +
                ", mondayUnDoneCount=" + mondayUnDoneCount +
                ", tuesdayDoneCount=" + tuesdayDoneCount +
                ", tuesdayUnDoneCount=" + tuesdayUnDoneCount +
                ", wednesdayDoneCount=" + wednesdayDoneCount +
                ", wednesdayUnDoneCount=" + wednesdayUnDoneCount +
                ", thursdayDoneCount=" + thursdayDoneCount +
                ", thursdayUnDoneCount=" + thursdayUnDoneCount +
                ", fridayDoneCount=" + fridayDoneCount +
                ", fridayUnDoneCount=" + fridayUnDoneCount +
                ", saturdayDoneCount=" + saturdayDoneCount +
                ", saturdayUnDoneCount=" + saturdayUnDoneCount +
                ", sundayDoneCount=" + sundayDoneCount +
                ", sundayUnDoneCount=" + sundayUnDoneCount +
                '}';
    }
}


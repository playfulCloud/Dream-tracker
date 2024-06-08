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
public class SingleDayAggregate {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID habitUUID;

    private int mostDone;
    private int actualCount;
    private String date;

}

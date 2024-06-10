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
public class BreaksAggregate {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID habitUUID;
    private int sumOfBreaks;
    private int breaksQuantity;
    private boolean isBreak;
}

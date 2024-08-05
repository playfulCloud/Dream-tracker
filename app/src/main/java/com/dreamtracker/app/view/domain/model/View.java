package com.dreamtracker.app.view.domain.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class View {

    //TODO: Will be changed in third iteration for now there is only habitUUID of one habit for testing api
    @Id
    @GeneratedValue
    private UUID id;
    private UUID userUUID;
    private String name;
    private String description;
    private boolean habits;
    private boolean stats;
    private boolean goals;

}

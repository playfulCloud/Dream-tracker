package com.dreamtracker.app.view.domain.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "Views")
public class View {

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

package com.dreamtracker.app.habit.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private UUID userUUID;
    @ManyToMany(mappedBy = "categories")
    @ToString.Exclude
   @JsonIgnore
    private List<Habit> habits;}

package com.dreamtracker.app.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToMany(mappedBy = "categories")
    private List<Habit> habits;}

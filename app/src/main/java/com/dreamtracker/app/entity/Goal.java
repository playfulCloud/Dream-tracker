package com.dreamtracker.app.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Goal {

    @Id
    @GeneratedValue
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

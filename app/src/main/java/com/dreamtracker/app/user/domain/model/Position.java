package com.dreamtracker.app.user.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Position {

  @Id @GeneratedValue public UUID id;

  private int habitX;
  private int habitY;

  private int goalX;
  private int goalY;

  private int statX;
  private int statY;

  private int chartX;
  private int chartY;

  private UUID userUUID;

  private boolean habitEnabled;
  private boolean goalEnabled;
  private boolean statsEnabled;
  private boolean chartsEnabled;
}

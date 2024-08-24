package com.dreamtracker.app.user.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

@Entity
@Data
public class Position {

  @Id @GeneratedValue public UUID id;

  public int habitX;
  public int habitY;

  public int goalX;
  public int goalY;

  public int statX;
  public int statY;

  public int chartX;
  public int chartY;

  public UUID userUUID;

  boolean habitEnabled;
  boolean goalEnabled;
  boolean statsEnabled;
  boolean chartsEnabled;
}

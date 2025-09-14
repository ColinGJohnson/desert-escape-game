package dev.cgj.desertescape.render;

import dev.cgj.desertescape.entity.CarType;

public record HudData(
  CarType carType,
  float speed,
  int health,
  int fuel,
  int rockets,
  int nitro,
  int shield,
  float progress,
  int distance,
  int score
) {};

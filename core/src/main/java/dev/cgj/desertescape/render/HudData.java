package dev.cgj.desertescape.render;

import dev.cgj.desertescape.entity.CarType;

public record HudData(
  CarType carType,
  float speed,
  float distance,
  float fuel,
  int health,
  int rockets,
  int nitro,
  int shield,
  int score
) {}

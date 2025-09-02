package dev.cgj.games.escape.render;

import dev.cgj.games.escape.entity.CarType;

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

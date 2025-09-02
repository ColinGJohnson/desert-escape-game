package dev.cgj.games.escape.entity;

public enum CarType {
  SEDAN("/sprites/vehicles/sedan.png"),
  SPORTS("/sprites/vehicles/sports_car.png"),
  VAN("/sprites/vehicles/van.png"),
  TRUCK("/sprites/vehicles/truck.png");

  final String spritePath;

  CarType(String spritePath) {
    this.spritePath = spritePath;
  }
}

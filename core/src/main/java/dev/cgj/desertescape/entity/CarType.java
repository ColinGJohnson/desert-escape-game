package dev.cgj.desertescape.entity;

public enum CarType {
  SEDAN("/sprites/vehicles/sedan.png", 14f, -7f, 1.6f, 18f, 0.9f, 18f, 1.0f, 100, 100f),
  SPORTS("/sprites/vehicles/sports_car.png", 16f, -8f, 2f, 20f, 1f, 20f, 1.2f, 8, 80f),
  VAN("/sprites/vehicles/van.png", 12f, -6f, 1.4f, 16f, 0.8f, 16f, 0.9f, 12, 120f),
  TRUCK("/sprites/vehicles/truck.png", 10f, -5f, 1.2f, 14f, 0.7f, 14f, 0.8f, 15, 150f);

  public final String spritePath;
  public final float maxForwardSpeed;
  public final float maxBackwardSpeed;
  public final float maxDriveForce;
  public final float maxLateralImpulse;
  public final float maxBrakeImpulse;
  public final float maxSteerAngleDeg;
  public final float fuelLossRate;
  public final int maxHealth;
  public final float maxFuel;

  CarType(
    String spritePath,
    float maxForwardSpeed,
    float maxBackwardSpeed,
    float maxDriveForce,
    float maxLateralImpulse,
    float maxBrakeImpulse,
    float maxSteerAngleDeg,
    float fuelLossRate,
    int maxHealth,
    float maxFuel
  ) {
    this.spritePath = spritePath;
    this.maxForwardSpeed = maxForwardSpeed;
    this.maxBackwardSpeed = maxBackwardSpeed;
    this.maxDriveForce = maxDriveForce;
    this.maxLateralImpulse = maxLateralImpulse;
    this.maxBrakeImpulse = maxBrakeImpulse;
    this.maxSteerAngleDeg = maxSteerAngleDeg;
    this.fuelLossRate = fuelLossRate;
    this.maxHealth = maxHealth;
    this.maxFuel = maxFuel;
  }
}

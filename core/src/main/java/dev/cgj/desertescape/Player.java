package dev.cgj.desertescape;

import dev.cgj.desertescape.vehicle.Car;

import static dev.cgj.desertescape.KeyMap.KeyBinding.ACCELERATE;
import static dev.cgj.desertescape.KeyMap.KeyBinding.BRAKE_HAND;
import static dev.cgj.desertescape.KeyMap.KeyBinding.STEER_LEFT;
import static dev.cgj.desertescape.KeyMap.KeyBinding.STEER_RIGHT;

public record Player(Car car, Inventory inventory, ScoreBoard scoreBoard) {

  public void update(float delta) {
    car.update(delta);
    scoreBoard.updateDistance(car.body.carBody.getPosition().y);
  }

  public void handleInput(float delta) {
    car.steer(delta, getSteeringInput());

    if (KeyMap.isPressed(ACCELERATE)) {
      car.accelerate(1);
    }

    if (KeyMap.isPressed(BRAKE_HAND)) {
      car.brake(1);
    }
  }

  private float getSteeringInput() {
    float steerInput = 0;
    if (KeyMap.isPressed(STEER_LEFT)) {
      steerInput += 1;
    }
    if (KeyMap.isPressed(STEER_RIGHT)) {
      steerInput -= 1;
    }
    return steerInput;
  }
}

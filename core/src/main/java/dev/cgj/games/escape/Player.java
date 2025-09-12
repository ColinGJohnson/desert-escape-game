package dev.cgj.games.escape;

import dev.cgj.games.escape.entity.Car;
import dev.cgj.games.escape.entity.CollisionHandler;

public class Player implements CollisionHandler {
  private final Car car;
  private final Inventory inventory;

  public Player(Car car, Inventory inventory) {
    this.car = car;
    this.inventory = inventory;
  }

  public void update(float delta) {
    car.update(delta);
    car.updatePhysics();
  }

  public Car getCar() {
    return car;
  }

  public Inventory getInventory() {
    return inventory;
  }
}

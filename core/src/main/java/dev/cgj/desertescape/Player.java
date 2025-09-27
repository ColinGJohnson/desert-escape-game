package dev.cgj.desertescape;

import dev.cgj.desertescape.entity.Car;

public class Player {
  private final Car car;
  private final Inventory inventory;
  private final ScoreBoard scoreBoard;

  public Player(Car car, Inventory inventory, ScoreBoard scoreBoard) {
    this.car = car;
    this.inventory = inventory;
    this.scoreBoard = scoreBoard;
  }

  public void update(float delta) {
    car.update(delta);
    car.updatePhysics();
    scoreBoard.updateDistance(car.body.carBody.getPosition().y);
  }

  public Car getCar() {
    return car;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public ScoreBoard getScoreBoard() {
    return scoreBoard;
  }
}

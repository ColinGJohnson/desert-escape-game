package dev.cgj.games.escape;

import dev.cgj.games.escape.entity.Car;
import dev.cgj.games.escape.entity.CollisionHandler;
import dev.cgj.games.escape.physics.UserData;

public class Player implements CollisionHandler {
  public Player(Car car) {
    this.car = car;
    car.setUserData(new UserData((object) -> {}, this));
  }

  int health = 10;

  int fuel ;

  int rockets;

  int nitro;

  int shield;

  Car car;

  public void damage(int amount) {
    health = Math.max(0, health - amount);
  }
}

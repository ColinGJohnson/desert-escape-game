package dev.cgj.desertescape;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.desertescape.physics.UserData;
import dev.cgj.desertescape.vehicle.Car;
import dev.cgj.desertescape.vehicle.CarType;

import static dev.cgj.desertescape.KeyMap.KeyBinding.ACCELERATE;
import static dev.cgj.desertescape.KeyMap.KeyBinding.BRAKE_HAND;
import static dev.cgj.desertescape.KeyMap.KeyBinding.REVERSE;
import static dev.cgj.desertescape.KeyMap.KeyBinding.STEER_LEFT;
import static dev.cgj.desertescape.KeyMap.KeyBinding.STEER_RIGHT;

public record Player(Car car, Inventory inventory, ScoreBoard scoreBoard) {

  public Player(World world, Inventory inventory, ScoreBoard scoreBoard) {
    this(new Car(CarType.SPORTS, world), inventory, scoreBoard);
    car.setUserData(UserData.dataOnly(this));
  }

  public void update(float delta) {
    car.update(delta);
    scoreBoard.updateDistance(car.body.getPosition().y);
  }

  public void handleInput(float delta) {
    car.steer(delta, getSteeringInput());

    if (KeyMap.isPressed(ACCELERATE)) {
      car.accelerate(1);
    } else if (KeyMap.isPressed(REVERSE)) {
      car.accelerate(-1);
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

  public void draw(SpriteBatch renderBatch) {
    car.draw(renderBatch);
  }
}

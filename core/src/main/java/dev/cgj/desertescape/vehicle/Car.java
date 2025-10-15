package dev.cgj.desertescape.vehicle;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.physics.CarBody;
import dev.cgj.desertescape.physics.UserData;

import static dev.cgj.desertescape.util.SpriteUtil.drawAtBodyPosition;
import static dev.cgj.desertescape.util.SpriteUtil.getScaledSprite;

/**
 * A car entity with physics implementation.
 * Uses physics adapted from <a href="https://www.iforce2d.net/b2dtut/top-down-car">this iforce2d article</a>.
 */
public class Car implements Disposable {
  public final Sprite sprite;
  public final CarBody carBody;
  public final CarType carType;

  private int health;
  private float fuel;

  public Car(CarType carType, World world) {
    this.carType = carType;
    this.health = carType.maxHealth;
    this.fuel = carType.maxFuel;
    carBody = new CarBody(world);
    sprite = getScaledSprite(carType.spritePath);
  }

  @Override
  public void dispose() {
    sprite.getTexture().dispose();
  }

  public void draw(SpriteBatch batch) {
    drawAtBodyPosition(batch, sprite, carBody.carBody);
  }

  public void update(float delta) {
    updateFuel(delta);
    carBody.update(carType);
  }

  private void updateFuel(float delta) {
    fuel -= carType.fuelLossRate * delta;
    if (fuel <= 0) {
      health = 0;
      fuel = 0;
    } else if (fuel > carType.maxFuel) {
      fuel = carType.maxFuel;
    }
  }

  /**
   * Accelerates the car towards its maximum forward speed based on the input value.
   *
   * @param input The acceleration input, where -1 full reverse, and 1 represents full forwards
   *              acceleration. The input value is clamped between 0 and 1.
   */
  public void accelerate(float input) {
    float clampedInput = MathUtils.clamp(input, -1, 1f);
    if (clampedInput < 0) {
      carBody.accelerateToSpeed(carType.maxBackwardSpeed, Math.abs(clampedInput) * carType.maxDriveForce);
    } else {
      carBody.accelerateToSpeed(carType.maxForwardSpeed, clampedInput * carType.maxDriveForce);
    }
  }

  /**
   * Reduces the car's forward velocity based on the brake input.
   * Applies a braking force proportional to the input value and limited by the maximum brake impulse.
   *
   * @param input The brake input, where 0 represents no braking, and 1 represents full braking.
   *              The value will be clamped between 0 and 1.
   */
  public void brake(float input) {
    float clampedInput = MathUtils.clamp(input, 0, 1f);
    carBody.brake(clampedInput * carType.maxBrakeImpulse);
  }

  /**
   * Adjusts the steering angle of the car based on the input and time elapsed.
   *
   * @param delta The time elapsed since the last update, used to smooth steering changes.
   * @param input The desired steering input, where -1 represents full left, 1 represents full right,
   *              and 0 represents no steering. The value will be clamped between -1 and 1.
   */
  public void steer(float delta, float input) {
    float clampedInput = MathUtils.clamp(input, -1, 1f);
    carBody.turnWheels(delta, clampedInput * (float) Math.toRadians(carType.maxSteerAngleDeg));
  }

  public void damage(int amount) {
    health = Math.max(0, health - amount);
  }

  public void repair(int amount) {
    health = Math.min(carType.maxHealth, health + amount);
  }

  public void refuel(float amount) {
    fuel = Math.min(carType.maxFuel, fuel + amount);
  }

  public int getHealth() {
    return health;
  }

  public float getFuel() {
    return fuel;
  }

  public Vector2 getPosition() {
    return carBody.getPosition().cpy();
  }

  public void setUserData(UserData userData) {
    carBody.setUserData(userData);
  }
}

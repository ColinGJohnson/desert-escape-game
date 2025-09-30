package dev.cgj.desertescape.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.desertescape.physics.UserData;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;

/// Uses physics adapted from [this iforce2d article](https://www.iforce2d.net/b2dtut/top-down-car).
public class Car implements Disposable {
  public final Sprite sprite;
  public final CarBody body;
  public final CarType type;

  private int health;
  private float fuel;

  public Car(CarType type, World world) {
    this.type = type;
    this.health = type.maxHealth;
    this.fuel = type.maxFuel;
    body = new CarBody(world, new UserData((object) -> {}, this));

    Texture texture = new Texture(type.spritePath.substring(1));
    sprite = new Sprite(texture);
    sprite.setSize(texture.getWidth() * SPRITE_TO_WORLD,
      texture.getHeight() * SPRITE_TO_WORLD);
    sprite.setOriginCenter();
  }

  @Override
  public void dispose() {
    sprite.getTexture().dispose();
  }

  public void draw(SpriteBatch batch) {
    float posX = body.carBody.getPosition().x;
    float posY = body.carBody.getPosition().y;
    sprite.setCenter(posX, posY);
    sprite.setRotation(body.carBody.getAngle() * MathUtils.radiansToDegrees);
    sprite.draw(batch);
  }

  public void update(float delta) {
    fuel -= type.fuelLossRate * delta;
    if (fuel <= 0) {
      health = 0;
      fuel = 0;
    } else if (fuel > type.maxFuel) {
      fuel = type.maxFuel;
    }
  }

  public void updatePhysics() {
    body.cancelLateralVelocity(type.maxLateralImpulse);
    body.cancelAngularVelocity();
  }

  /**
   * Accelerates the car towards its maximum forward speed based on the input value.
   *
   * @param input The acceleration input, where 0 represents no acceleration, and 1 represents full
   *              acceleration. The input value is clamped between 0 and 1.
   */
  public void accelerate(float input) {
    float clampedInput = MathUtils.clamp(input, 0, 1f);
    body.accelerateToSpeed(type.maxForwardSpeed, clampedInput * type.maxDriveForce);
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
    body.cancelForwardVelocity(clampedInput * type.maxBrakeImpulse);
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
    body.turnWheels(delta, clampedInput * (float) Math.toRadians(type.maxSteerAngleDeg));
  }

  public void damage(int amount) {
    health = Math.max(0, health - amount);
  }

  public int getHealth() {
    return health;
  }

  public float getFuel() {
    return fuel;
  }
}

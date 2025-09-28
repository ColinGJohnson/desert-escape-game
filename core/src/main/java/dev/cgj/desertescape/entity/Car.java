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
  private static final float MAX_FORWARD_SPEED = 16f;
  private static final float MAX_BACKWARD_SPEED = -8f;
  private static final float MAX_DRIVE_FORCE = 2f;
  private static final float MAX_LATERAL_IMPULSE = 20f;
  private static final float MAX_BRAKE_IMPULSE = 1f;
  private static final float FUEL_LOSS_RATE = 1f;
  private static final float MAX_STEER_ANGLE = 20f;

  private int health = 10;
  private float fuel = 100;

  public final Sprite sprite;
  public final CarType carType;
  public final CarBody body;

  public Car(CarType carType, World world) {
    this.carType = carType;
    body = new CarBody(world, new UserData((object) -> {}, this));

    Texture texture = new Texture("sprites/vehicles/sports_car.png");
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
    fuel -= FUEL_LOSS_RATE * delta;
    if (fuel <= 0) {
      health = 0;
      fuel = 0;
    }
  }

  public void updatePhysics() {
    body.cancelLateralVelocity(MAX_LATERAL_IMPULSE);
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
    body.accelerateToSpeed(MAX_FORWARD_SPEED, clampedInput * MAX_DRIVE_FORCE);
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
    body.cancelForwardVelocity(clampedInput * MAX_BRAKE_IMPULSE);
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
    body.turnWheels(delta, clampedInput * (float) Math.toRadians(MAX_STEER_ANGLE));
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

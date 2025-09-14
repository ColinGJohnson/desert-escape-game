package dev.cgj.desertescape.entity;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
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

  public void handleInput(float delta, Input input) {
    if (input.isKeyPressed(Keys.UP) || input.isKeyPressed(Keys.W)) {
      body.accelerateToSpeed(MAX_FORWARD_SPEED, MAX_DRIVE_FORCE);
    } else if (input.isKeyPressed(Keys.DOWN) || input.isKeyPressed(Keys.S)) {
      body.accelerateToSpeed(MAX_BACKWARD_SPEED, MAX_DRIVE_FORCE);
    }

    boolean turnLeft = input.isKeyPressed(Keys.LEFT) || input.isKeyPressed(Keys.A);
    boolean turnRight = input.isKeyPressed(Keys.RIGHT) || input.isKeyPressed(Keys.D);

    if (turnLeft && !turnRight) {
      body.turnWheels(delta, (float) Math.toRadians(20));
    } else if (turnRight && !turnLeft) {
      body.turnWheels(delta, (float) Math.toRadians(-20));
    } else {
      body.turnWheelsImmediately(0);
    }

    if (input.isKeyPressed(Keys.SPACE)) {
      body.cancelForwardVelocity(MAX_BRAKE_IMPULSE);
    }
  }

  public void updatePhysics() {
    body.cancelLateralVelocity(MAX_LATERAL_IMPULSE);
    body.cancelAngularVelocity();
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

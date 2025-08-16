package dev.cgj.games.escape.entity;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.games.old.CarType;

import static dev.cgj.games.escape.Constants.SPRITE_TO_WORLD;

/// Uses physics adapted from [this iforce2d article](https://www.iforce2d.net/b2dtut/top-down-car).
public class Car implements Disposable {
  private static final float TURN_TORQUE = 3f;
  private static final float MAX_FORWARD_SPEED = 10f;
  private static final float MAX_BACKWARD_SPEED = -4f;
  private static final float MAX_DRIVE_FORCE = 10f;
  private static final float MAX_LATERAL_IMPULSE = 3f;
  private static final float MAX_BRAKE_IMPULSE = 1f;

  public final Texture texture;
  public final Sprite sprite;
  public final CarType carType;
  public final CarBody body;

  public Car(CarType carType, World world) {
    this.carType = carType;
    texture = new Texture("sprites/vehicles/sports_car.png");
    sprite = new Sprite(texture);
    sprite.setSize(texture.getWidth() * SPRITE_TO_WORLD,
      texture.getHeight() * SPRITE_TO_WORLD);
    sprite.setOriginCenter();
    body = new CarBody(world);
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

  public void handleInput(Input input) {
    if (input.isKeyPressed(Keys.UP) || input.isKeyPressed(Keys.W)) {
      accelerateToSpeed(MAX_FORWARD_SPEED);
    } else if (input.isKeyPressed(Keys.DOWN) || input.isKeyPressed(Keys.S)) {
      accelerateToSpeed(MAX_BACKWARD_SPEED);
    }

    boolean turnLeft = input.isKeyPressed(Keys.LEFT) || input.isKeyPressed(Keys.A);
    boolean turnRight = input.isKeyPressed(Keys.RIGHT) || input.isKeyPressed(Keys.D);

    if (turnLeft && !turnRight) {
      body.carBody.applyTorque(TURN_TORQUE, true);
    } else if (turnRight && !turnLeft) {
      body.carBody.applyTorque(-TURN_TORQUE, true);
    }

    if (input.isKeyPressed(Keys.SPACE)) {
      cancelForwardVelocity();
    }
  }

  public void updatePhysics() {
    cancelLateralVelocity();
    cancelAngularVelocity();
  }

  private void cancelAngularVelocity() {
    float impulse = -body.carBody.getAngularVelocity() * body.carBody.getInertia() * 0.1f;
    body.carBody.applyAngularImpulse(impulse, true);
  }

  private void accelerateToSpeed(float speed) {
    Vector2 forwardNormal = body.carBody.getWorldVector(new Vector2(0, 1)).cpy();
    float currentSpeed = getForwardVelocity().dot(forwardNormal);
    float force = (currentSpeed < speed) ? MAX_DRIVE_FORCE : -MAX_DRIVE_FORCE;
    body.carBody.applyForceToCenter(forwardNormal.scl(force), true);
  }

  private void cancelLateralVelocity() {
    cancelVelocity(getLateralVelocity(), MAX_LATERAL_IMPULSE);
  }

  private void cancelForwardVelocity() {
    cancelVelocity(getForwardVelocity(), MAX_BRAKE_IMPULSE);
  }

  private void cancelVelocity(Vector2 velocity, float maxImpulse) {
    Vector2 impulse = velocity.scl(-body.carBody.getMass());
    if (impulse.len() > maxImpulse) {
      impulse.scl(maxImpulse / impulse.len());
    }
    body.carBody.applyLinearImpulse(impulse, body.carBody.getWorldCenter(), true);
  }

  private Vector2 getLateralVelocity() {
    Vector2 rightNormal = body.carBody.getWorldVector(new Vector2(1, 0));
    return rightNormal.scl(rightNormal.dot(body.carBody.getLinearVelocity()));
  }

  private Vector2 getForwardVelocity() {
    Vector2 forwardNormal = body.carBody.getWorldVector(new Vector2(0, 1));
    return forwardNormal.scl(forwardNormal.dot(body.carBody.getLinearVelocity()));
  }
}

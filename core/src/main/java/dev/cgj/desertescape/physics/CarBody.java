package dev.cgj.desertescape.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.desertescape.entity.CarType;

import java.util.List;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;

public class CarBody {
  private static final float WHEEL_FRICTION = 0.4f;

  private final Body body;
  private final WheelBody frontLeftWheel;
  private final WheelBody frontRightWheel;
  private final WheelBody rearLeftWheel;
  private final WheelBody rearRightWheel;

  public CarBody(World world) {
    body = createCarBody(world);
    frontLeftWheel = new WheelBody(body, new Vector2(-4, 4), new Vector2(0.5f, 1.5f));
    frontRightWheel = new WheelBody(body, new Vector2(4, 4), new Vector2(0.5f, 1.5f));
    rearLeftWheel = new WheelBody(body, new Vector2(-4, -4), new Vector2(0.5f, 1.5f));
    rearRightWheel = new WheelBody(body, new Vector2(4, -4), new Vector2(0.5f, 1.5f));
  }

  public List<WheelBody> getWheels() {
    return List.of(frontLeftWheel, frontRightWheel, rearLeftWheel, rearRightWheel);
  }

  public void update(CarType type) {
    for (WheelBody wheel : getWheels()) {
      wheel.applyFriction(WHEEL_FRICTION);
      wheel.cancelLateralVelocity(type.maxLateralImpulse);
      wheel.cancelAngularVelocity();
    }
  }

  public float getForwardVelocity() {
    return BodyUtils.getForwardVelocity(body).len();
  }

  public void turnWheels(float delta, float desiredAngle) {
    frontRightWheel.turn(delta, desiredAngle);
    frontLeftWheel.turn(delta, desiredAngle);
  }

  // TODO: 4 wheel drive?
  public void accelerateToSpeed(float speed, float maxDriveForce) {
    frontLeftWheel.accelerateToSpeed(speed, maxDriveForce);
    frontRightWheel.accelerateToSpeed(speed, maxDriveForce);
  }

  public Vector2 getPosition() {
    return body.getPosition().cpy();
  }

  public void brake(float maxImpulse) {
    for (WheelBody wheel : getWheels()) {
      wheel.brake(maxImpulse);
    }
  }

  public void setUserData(UserData userData) {
    body.setUserData(userData);
  }

  private Body createCarBody(World world) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(10, 10);
    Body car = world.createBody(bodyDef);

    // The sports car sprite is 8 x 16 pixels, so the half-size is 4 x 8
    PolygonShape shape = new PolygonShape();
    shape.setAsBox(3f * SPRITE_TO_WORLD, 8f * SPRITE_TO_WORLD);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 1f;
    car.createFixture(fixtureDef);

    return car;
  }

  public Body getBody() {
    return body;
  }
}

package dev.cgj.desertescape.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.desertescape.vehicle.CarType;

import java.util.List;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;

public class CarBody {
  public Body carBody;
  public WheelBody frontLeftWheel;
  public WheelBody frontRightWheel;
  public WheelBody rearLeftWheel;
  public WheelBody rearRightWheel;

  public List<WheelBody> getWheels() {
    return List.of(frontLeftWheel, frontRightWheel, rearLeftWheel, rearRightWheel);
  }

  public CarBody(World world) {
    carBody = createCarBody(world);

    frontLeftWheel = new WheelBody(world);
    frontLeftWheel.joinToVehicle(new Vector2(-4, 4), carBody);

    frontRightWheel = new WheelBody(world);
    frontRightWheel.joinToVehicle(new Vector2(4, 4), carBody);

    rearLeftWheel = new WheelBody(world);
    rearLeftWheel.joinToVehicle(new Vector2(-4, -4), carBody);

    rearRightWheel = new WheelBody(world);
    rearRightWheel.joinToVehicle(new Vector2(4, -4), carBody);
  }

  public void update(CarType type) {
    for (WheelBody wheel : getWheels()) {
      wheel.update(type);
    }
  }

  public float getForwardVelocity() {
    return BodyUtils.getForwardVelocity(carBody).len();
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
    return carBody.getPosition().cpy();
  }

  public void brake(float maxImpulse) {
    for (WheelBody wheel : getWheels()) {
      wheel.brake(maxImpulse);
    }
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

  public void setUserData(UserData userData) {
    carBody.setUserData(userData);
  }
}

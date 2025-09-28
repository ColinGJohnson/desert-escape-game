package dev.cgj.desertescape.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import dev.cgj.desertescape.physics.UserData;

import java.util.List;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;
import static dev.cgj.desertescape.Constants.spriteToWorld;

public class CarBody {
  public Body carBody;

  public Body frontLeftWheel;
  public RevoluteJoint frontLeftWheelJoint;

  public Body frontRightWheel;
  public RevoluteJoint frontRightWheelJoint;

  public Body rearLeftWheel;
  public RevoluteJoint rearLeftWheelJoint;

  public Body rearRightWheel;
  public RevoluteJoint rearRightWheelJoint;

  public List<Body> getWheels() {
    return List.of(frontLeftWheel, frontRightWheel, rearLeftWheel, rearRightWheel);
  }

  public CarBody(World world, UserData userData) {
    carBody = createCarBody(world, userData);

    frontLeftWheel = createWheel(world);
    frontLeftWheelJoint = joinWheel(world, new Vector2(-4, 4), carBody, frontLeftWheel);

    frontRightWheel = createWheel(world);
    frontRightWheelJoint = joinWheel(world, new Vector2(4, 4), carBody, frontRightWheel);

    rearLeftWheel = createWheel(world);
    rearLeftWheelJoint = joinWheel(world, new Vector2(-4, -4), carBody, rearLeftWheel);

    rearRightWheel = createWheel(world);
    rearRightWheelJoint = joinWheel(world, new Vector2(4, -4), carBody, rearRightWheel);
  }

  public float getForwardVelocity() {
    return getForwardVelocity(carBody).len();
  }

  public void turnWheels(float delta, float desiredAngle) {
    float currentAngle = frontLeftWheelJoint.getJointAngle();
    float maxTurn = (float) Math.toRadians(180) * delta;
    float newAngle = currentAngle + Math.clamp(desiredAngle - currentAngle, -maxTurn, maxTurn);
    turnWheelsImmediately(newAngle);
  }

  public void turnWheelsImmediately(float DesiredAngle) {
    frontRightWheelJoint.setLimits(DesiredAngle, DesiredAngle);
    frontLeftWheelJoint.setLimits(DesiredAngle, DesiredAngle);
  }

  public void accelerateToSpeed(float speed, float maxDriveForce) {
    accelerateToSpeed(frontLeftWheel, speed, maxDriveForce);
    accelerateToSpeed(frontRightWheel, speed, maxDriveForce);

    // TODO: Fix all wheel drive steering

    //    for (Body wheel : getWheels()) {
    //      accelerateToSpeed(wheel, speed, maxDriveForce);
    //    }
  }

  private void accelerateToSpeed(Body body, float desiredSpeed, float maxDriveForce) {
    Vector2 forwardNormal = body.getWorldVector(new Vector2(0, 1)).cpy();
    float currentSpeed = getForwardVelocity(body).dot(forwardNormal);
    if (Math.abs(currentSpeed - desiredSpeed) > 0.01) {
      float force = (currentSpeed < desiredSpeed) ? maxDriveForce : -maxDriveForce;
      body.applyForceToCenter(forwardNormal.scl(force), true);
    }
  }

  public void cancelAngularVelocity() {
    getWheels().forEach(this::cancelAngularVelocity);
  }

  private void cancelAngularVelocity(Body body) {
    float impulse = -body.getAngularVelocity() * body.getInertia() * 1f;
    body.applyAngularImpulse(impulse, true);
  }

  public void cancelLateralVelocity(float maxImpulse) {
    getWheels().forEach(wheel -> cancelVelocity(wheel, getLateralVelocity(wheel), maxImpulse));
  }

  public void cancelForwardVelocity(float maxImpulse) {
    getWheels().forEach(wheel -> cancelVelocity(wheel, getForwardVelocity(wheel), maxImpulse));
  }

  private void cancelVelocity(Body body, Vector2 velocity, float maxImpulse) {
    Vector2 impulse = velocity.scl(-body.getMass());
    if (impulse.len() > maxImpulse) {
      impulse.scl(maxImpulse / impulse.len());
    }
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  private Vector2 getLateralVelocity(Body body) {
    Vector2 rightNormal = body.getWorldVector(new Vector2(1, 0));
    return rightNormal.scl(rightNormal.dot(body.getLinearVelocity()));
  }

  private Vector2 getForwardVelocity(Body body) {
    Vector2 forwardNormal = body.getWorldVector(new Vector2(0, 1));
    return forwardNormal.scl(forwardNormal.dot(body.getLinearVelocity()));
  }

  private RevoluteJoint joinWheel(World world, Vector2 anchor, Body car, Body wheel) {
    RevoluteJointDef jointDef = new RevoluteJointDef();
    jointDef.enableLimit = true;
    jointDef.lowerAngle = 0;
    jointDef.upperAngle = 0;
    jointDef.bodyA = car;
    jointDef.bodyB = wheel;
    jointDef.localAnchorB.setZero();
    jointDef.localAnchorA.set(spriteToWorld(anchor));
    return (RevoluteJoint) world.createJoint(jointDef);
  }

  private Body createCarBody(World world, UserData userData) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(10, 10);
    Body car = world.createBody(bodyDef);
    car.setUserData(userData);

    // The sports car sprite is 8 x 16 pixels, so the half-size is 4 x 8
    PolygonShape shape = new PolygonShape();
    shape.setAsBox(3f * SPRITE_TO_WORLD, 8f * SPRITE_TO_WORLD);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 1f;
    car.createFixture(fixtureDef);

    return car;
  }

  private Body createWheel(World world) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(10, 10);
    Body wheel = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(0.5f * SPRITE_TO_WORLD, 1.5f * SPRITE_TO_WORLD);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 1f;
    wheel.createFixture(fixtureDef);

    return wheel;
  }
}

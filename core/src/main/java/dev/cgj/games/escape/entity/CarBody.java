package dev.cgj.games.escape.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import static dev.cgj.games.escape.Constants.SPRITE_TO_WORLD;
import static dev.cgj.games.escape.Constants.spriteToWorld;

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

  public CarBody(World world) {
    carBody = createCarBody(world);

    frontLeftWheel = createWheel(world);
    frontLeftWheelJoint = joinWheel(world, new Vector2(-4, 4), carBody, frontLeftWheel);

    frontRightWheel = createWheel(world);
    frontRightWheelJoint = joinWheel(world, new Vector2(4, 4), carBody, frontRightWheel);

    rearLeftWheel = createWheel(world);
    rearLeftWheelJoint = joinWheel(world, new Vector2(-4, -4), carBody, rearLeftWheel);

    rearRightWheel = createWheel(world);
    rearRightWheelJoint = joinWheel(world, new Vector2(4, -4), carBody, rearRightWheel);
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

  private Body createCarBody(World world) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(10, 10);
    Body car = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();

    // The sports car sprite is 8 x 16 pixels, so the half-size is 4 x 8
    shape.setAsBox(3f * SPRITE_TO_WORLD, 8f * SPRITE_TO_WORLD);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 0.1f;
    car.createFixture(fixtureDef);

    return car;
  }

  private Body createWheel(World world) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(10, 10);dw
    Body wheel = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(0.5f * SPRITE_TO_WORLD, 1.5f * SPRITE_TO_WORLD);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 0.1f;
    wheel.createFixture(fixtureDef);

    return wheel;
  }
}

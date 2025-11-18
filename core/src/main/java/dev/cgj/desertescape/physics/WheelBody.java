package dev.cgj.desertescape.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import java.util.ArrayList;
import java.util.List;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;
import static dev.cgj.desertescape.Constants.spriteToWorld;
import static dev.cgj.desertescape.physics.BodyUtils.cancelVelocity;
import static dev.cgj.desertescape.physics.BodyUtils.getForwardVelocity;
import static dev.cgj.desertescape.physics.BodyUtils.getLateralVelocity;

public class WheelBody {

  /**
   * Frequency in milliseconds at which to update the position history.
   */
  private static final float POSITION_HISTORY_INTERVAL = 10f;

  /**
   * Maximum number of entries to keep in this wheel's position history.
   */
  private static final float POSITION_HISTORY_LENGTH = 100f;

  /**
   * Represents a wheel or tread that can more forwards and backwards, but that resists lateral or
   * angular skidding.
   */
  private final Body wheel;

  /**
   * Joins the wheel to the body of a vehicle.
   */
  private RevoluteJoint joint;

  /**
   * History of wheel positions for tracking movement.
   */
  private final List<Vector2> positionHistory;

  /**
   * Timer to track when to record next position.
   */
  private float positionTimer;

  public WheelBody(Body body, Vector2 anchor, Vector2 size) {
    wheel = createWheel(body.getWorld(), size);
    joinToVehicle(anchor, body);
    positionHistory = new ArrayList<>();
    positionTimer = 0f;
  }

  public void update(float delta) {
    positionTimer += delta;

    if (positionTimer >= POSITION_HISTORY_INTERVAL) {
      Vector2 currentPosition = wheel.getPosition().cpy();
      positionHistory.add(currentPosition);

      while (positionHistory.size() > POSITION_HISTORY_LENGTH) {
        positionHistory.removeFirst();
      }

      positionTimer = 0f;
    }
  }

  public void brake(float maxImpulse) {
    cancelVelocity(wheel, getForwardVelocity(wheel), maxImpulse);
  }

  public void turn(float delta, float desiredAngle) {
    float currentAngle = joint.getJointAngle();
    float maxTurn = (float) Math.toRadians(180) * delta;
    float newAngle = currentAngle + Math.clamp(desiredAngle - currentAngle, -maxTurn, maxTurn);
    joint.setLimits(newAngle, newAngle);
  }

  public void applyForceForwards(float maxForce) {
    Vector2 forwardNormal = wheel.getWorldVector(new Vector2(0, 1)).cpy();
    wheel.applyForceToCenter(forwardNormal.scl(maxForce), true);
  }

  public void accelerateToSpeed(float desiredSpeed, float maxDriveForce) {
    Vector2 forwardNormal = wheel.getWorldVector(new Vector2(0, 1)).cpy();
    float currentSpeed = getForwardVelocity(wheel).dot(forwardNormal);
    if (Math.abs(currentSpeed - desiredSpeed) > 0.01) {
      float force = (currentSpeed < desiredSpeed) ? maxDriveForce : -maxDriveForce;
      wheel.applyForceToCenter(forwardNormal.scl(force), true);
    }
  }

  public void joinToVehicle(Vector2 anchor, Body car) {
    RevoluteJointDef jointDef = new RevoluteJointDef();
    jointDef.enableLimit = true;
    jointDef.lowerAngle = 0;
    jointDef.upperAngle = 0;
    jointDef.bodyA = car;
    jointDef.bodyB = wheel;
    jointDef.localAnchorB.setZero();
    jointDef.localAnchorA.set(spriteToWorld(anchor));
    joint = (RevoluteJoint) wheel.getWorld().createJoint(jointDef);
  }

  public void applyFriction(float friction) {
    // TODO: Vary friction depending on wheel+car mass
    Vector2 frictionForce = wheel.getLinearVelocity().cpy().nor().scl(-1 * friction);
    wheel.applyForceToCenter(frictionForce, true);
  }

  public void cancelLateralVelocity(float maxLateralImpulse) {
    cancelVelocity(wheel, getLateralVelocity(wheel), maxLateralImpulse);
  }

  public void cancelAngularVelocity() {
    float impulse = -wheel.getAngularVelocity() * wheel.getInertia() * 1f;
    wheel.applyAngularImpulse(impulse, true);
  }

  // TODO: Wheel bodies should not participate in collisions
  private Body createWheel(World world, Vector2 size) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    Body wheel = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(size.x * SPRITE_TO_WORLD, size.y * SPRITE_TO_WORLD);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 1f;
    wheel.createFixture(fixtureDef);

    return wheel;
  }

  public Body getBody() {
    return wheel;
  }
}

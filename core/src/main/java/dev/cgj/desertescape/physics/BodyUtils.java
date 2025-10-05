package dev.cgj.desertescape.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class BodyUtils {

  public static Vector2 getForwardNormal(Body body) {
    return body.getWorldVector(new Vector2(0, 1));
  }

  public static Vector2 getLateralVelocity(Body body) {
    Vector2 rightNormal = body.getWorldVector(new Vector2(1, 0));
    return rightNormal.scl(rightNormal.cpy().dot(body.getLinearVelocity()));
  }

  public static Vector2 getForwardVelocity(Body body) {
    Vector2 forwardNormal = getForwardNormal(body);
    return forwardNormal.scl(forwardNormal.cpy().dot(body.getLinearVelocity()));
  }

  public static void cancelVelocity(Body body, Vector2 velocity, float maxImpulse) {
    Vector2 impulse = velocity.scl(-body.getMass());
    if (impulse.len() > maxImpulse) {
      impulse.scl(maxImpulse / impulse.len());
    }
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }
}

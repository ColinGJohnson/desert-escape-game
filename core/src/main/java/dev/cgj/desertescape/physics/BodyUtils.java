package dev.cgj.desertescape.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;

public class BodyUtils {

  public static Vector2 getForwardNormal(Body body) {
    return body.getWorldVector(new Vector2(0, 1));
  }

  public static Vector2 getLateralVelocity(Body body) {
    Vector2 rightNormal = body.getWorldVector(new Vector2(1, 0));
    return rightNormal.scl(rightNormal.dot(body.getLinearVelocity()));
  }

  public static Vector2 getForwardVelocity(Body body) {
    Vector2 forwardNormal = getForwardNormal(body);
    return forwardNormal.scl(forwardNormal.dot(body.getLinearVelocity()));
  }

  public static void cancelVelocity(Body body, Vector2 velocity, float maxImpulse) {
    Vector2 impulse = velocity.cpy().scl(-body.getMass());
    if (impulse.len() > maxImpulse) {
      impulse.scl(maxImpulse / impulse.len());
    }
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  public static Body createStaticBody(World world, Vector2 position, Vector2 spriteSize) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(position);
    Body body = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    float hx = spriteSize.x * SPRITE_TO_WORLD / 2f;
    float hy = spriteSize.y * SPRITE_TO_WORLD / 2f;
    shape.setAsBox(hx, hy);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.isSensor = true;

    body.createFixture(fixtureDef);
    return body;
  }
}

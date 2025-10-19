package dev.cgj.desertescape.vehicle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.desertescape.Player;
import dev.cgj.desertescape.entity.Entity;
import dev.cgj.desertescape.physics.UserData;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;

public class TankShell extends Entity {
  public static final float SPEED = 10f;
  private static final Vector2 SIZE = new Vector2(2f, 4f);

  public TankShell(World world, Vector2 position, Vector2 velocity) {
    super("sprites/enemies/tankShell.png", createBody(world, position));
    getBody().setUserData(UserData.handlerOnly(this::handleCollision));
    getBody().setLinearVelocity(velocity);
  }

  public void update(float delta) {
    // The long edge of the rectangular shell should be parallel to its velocity vector
    float velocityAngle = getBody().getLinearVelocity().angleRad() + MathUtils.PI / 2;
    getBody().setTransform(getBody().getPosition(), velocityAngle);
  }

  public void handleCollision(Object entity) {
    if (isDestroyed()) {
      return;
    }

    switch (entity) {
      case Player player -> {
        player.car().damage(1);
        setDestroyed(true);
      }
      case null, default -> {
      }
    }
  }

  private static Body createBody(World world, Vector2 position) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(position);
    Body body = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    float hx = TankShell.SIZE.x * SPRITE_TO_WORLD / 2f;
    float hy = TankShell.SIZE.y * SPRITE_TO_WORLD / 2f;
    shape.setAsBox(hx, hy);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.isSensor = true;

    body.createFixture(fixtureDef);
    return body;
  }
}

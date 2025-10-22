package dev.cgj.desertescape.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.List;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;
import static dev.cgj.desertescape.physics.WheelBody.createAndJoinWheel;

public class TankBody {
  private static final float TRACK_FRICTION = 0.4f;
  private static final float TRACK_MAX_LATERAL_IMPULSE = 30f;

  private final Body body;
  private final WheelBody leftTrack;
  private final WheelBody rightTrack;

  public TankBody(World world) {
    body = createBody(world);
    leftTrack = createAndJoinWheel(body, new Vector2(-8, 0));
    rightTrack = createAndJoinWheel(body, new Vector2(8, 0));
  }

  public List<WheelBody> getTracks() {
    return List.of(leftTrack, rightTrack);
  }

  public void update(float delta) {
    for (WheelBody track : getTracks()) {
      track.applyFriction(TRACK_FRICTION);
      track.cancelLateralVelocity(TRACK_MAX_LATERAL_IMPULSE);
    }
  }

  private Body createBody(World world) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(15, 10);
    Body car = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(12f * SPRITE_TO_WORLD, 12f * SPRITE_TO_WORLD);

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

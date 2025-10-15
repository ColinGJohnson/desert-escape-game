package dev.cgj.desertescape.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;

public class TankBody {
  public Body body;

  public TankBody(World world) {
    body = createBody(world);
  }

  public void update(float delta) {

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

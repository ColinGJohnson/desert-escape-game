package dev.cgj.games.escape.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import dev.cgj.games.escape.physics.UserData;

import static dev.cgj.games.escape.Constants.SPRITE_TO_WORLD;

public class Obstacle implements Disposable {
  private final ObstacleType type;
  private final Sprite sprite;
  private final Body body;
  private final World world;
  private boolean consumed;

  public Obstacle(ObstacleType type, World world, Vector2 position) {
    this.type = type;
    body = createBody(world, position, new UserData(this::handleCollision));

    Texture texture = new Texture(type.getSpritePath());
    sprite = new Sprite(texture);
    sprite.setSize(texture.getWidth() * SPRITE_TO_WORLD,
      texture.getHeight() * SPRITE_TO_WORLD);
    sprite.setOriginCenter();
    this.world = world;
  }

  public void draw(SpriteBatch batch) {
    if (consumed) {
      return;
    }

    float posX = body.getPosition().x;
    float posY = body.getPosition().y;
    sprite.setCenter(posX, posY);
    sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
    sprite.draw(batch);
  }

  public void move(Vector2 delta) {
    Vector2 newPosition = body.getPosition().add(delta);
    body.setTransform(newPosition, body.getAngle());
  }

  @Override
  public void dispose() {
    sprite.getTexture().dispose();
    world.destroyBody(body);
  }

  private static Body createBody(World world, Vector2 position, UserData userData) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(position);
    Body body = world.createBody(bodyDef);
    body.setUserData(userData);

    // Asset sprites are 8x8 pixels, so the half size is 4px
    PolygonShape shape = new PolygonShape();
    shape.setAsBox(4f * SPRITE_TO_WORLD, 4f * SPRITE_TO_WORLD);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.isSensor = true;
    // fixtureDef.filter.set(new Filter());
    body.createFixture(fixtureDef);
    return body;
  }

  public void handleCollision(Object entity) {

    // TODO: Modify the fixture's filter to ignore collisions instead
    if (consumed) {
      return;
    }

    switch (entity) {
      case Car car -> {
        car.damage(1);
        consumed = true;
      }
      case null, default -> {}
    }
  }
}

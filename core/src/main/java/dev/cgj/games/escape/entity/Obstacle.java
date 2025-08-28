package dev.cgj.games.escape.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;

import static dev.cgj.games.escape.Constants.SPRITE_TO_WORLD;

public class Obstacle implements Disposable {
  private ObstacleType type;
  private Sprite sprite;
  private Body body;

  public Obstacle(ObstacleType type, World world, Vector2 position) {
    this.type = type;
    body = createBody(world, position);

    Texture texture = new Texture(type.getSpritePath());
    sprite = new Sprite(texture);
    sprite.setSize(texture.getWidth() * SPRITE_TO_WORLD,
        texture.getHeight() * SPRITE_TO_WORLD);
    sprite.setOriginCenter();
  }

  public void draw(SpriteBatch batch) {
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
  }

  private static Body createBody(World world, Vector2 position) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(position);
    Body body = world.createBody(bodyDef);

    // Asset sprites are 8x8 pixels, so the half size is 4px
    PolygonShape shape = new PolygonShape();
    shape.setAsBox(4f * SPRITE_TO_WORLD, 4f * SPRITE_TO_WORLD);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    body.createFixture(fixtureDef);
    return body;
  }
}

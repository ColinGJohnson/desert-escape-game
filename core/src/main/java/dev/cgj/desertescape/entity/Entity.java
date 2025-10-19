package dev.cgj.desertescape.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import static dev.cgj.desertescape.util.SpriteUtils.drawAtBodyPosition;
import static dev.cgj.desertescape.util.SpriteUtils.getScaledSprite;

public abstract class Entity implements Disposable {
  private final Sprite sprite;
  private final Body body;
  private boolean destroyed = false;

  public Entity(String spritePath, World world, Vector2 position) {
    sprite = getScaledSprite(spritePath);
    body = createBody(world, position);
  }

  public Body createBody(World world, Vector2 position) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(position);
    Body body = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(getSprite().getWidth() / 2, getSprite().getHeight() / 2);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.isSensor = true;

    body.createFixture(fixtureDef);
    return body;
  }

  @Override
  public void dispose() {
    sprite.getTexture().dispose();
    body.getWorld().destroyBody(body);
  }

  public void draw(SpriteBatch batch) {
    drawAtBodyPosition(batch, sprite, body);
  }

  public void move(Vector2 delta) {
    Vector2 newPosition = getBody().getPosition().add(delta);
    getBody().setTransform(newPosition, getBody().getAngle());
  }

  public Sprite getSprite() {
    return sprite;
  }

  public Body getBody() {
    return body;
  }

  public boolean isDestroyed() {
    return destroyed;
  }

  public void setDestroyed(boolean destroyed) {
    this.destroyed = destroyed;
  }
}

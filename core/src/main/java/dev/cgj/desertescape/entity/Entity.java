package dev.cgj.desertescape.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import java.util.List;

import static dev.cgj.desertescape.util.SpriteUtils.drawAtBodyPosition;
import static dev.cgj.desertescape.util.SpriteUtils.getScaledSprite;

public abstract class Entity implements Disposable {
  private final Sprite sprite;
  private final Body body;
  private boolean destroyed = false;

  public Entity(String spritePath, Body body) {
    sprite = getScaledSprite(spritePath);
    this.body = body;
  }

  @Override
  public void dispose() {
    sprite.getTexture().dispose();
    body.getWorld().destroyBody(body);
  }

  public void draw(SpriteBatch batch) {
    if (!destroyed) {
      drawAtBodyPosition(batch, sprite, body);
    }
  }

  public void move(Vector2 delta) {
    Vector2 newPosition = getBody().getPosition().add(delta);
    getBody().setTransform(newPosition, getBody().getAngle());
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

  public static void removeDestroyed(List<? extends Entity> entities) {
    for (Entity entity : entities) {
      if (entity.isDestroyed()) {
        entity.dispose();
      }
    }
    entities.removeIf(Entity::isDestroyed);
  }
}

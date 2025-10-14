package dev.cgj.desertescape.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import dev.cgj.desertescape.Constants;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;

public class SpriteUtil {

  /**
   * Creates and scales a sprite according to its pixel dimensions and the
   * {@link Constants#SPRITE_TO_WORLD} conversion factor.
   *
   * @param path The file path to the texture image for the sprite.
   * @return A Sprite object created from the texture at the provided path.
   */
  public static Sprite getScaledSprite(String path) {
    Texture texture = new Texture(path);
    Sprite sprite = new Sprite(texture);
    sprite.setSize(texture.getWidth() * SPRITE_TO_WORLD,
      texture.getHeight() * SPRITE_TO_WORLD);
    sprite.setOriginCenter();
    return sprite;
  }

  /**
   * Draws a sprite at the position and orientation of a given physics body.
   *
   * @param batch  The SpriteBatch used to draw the sprite.
   * @param sprite The Sprite to be rendered.
   * @param body   The Body whose position and angle will be used to position and rotate the sprite.
   */
  public static void drawAtBodyPosition(SpriteBatch batch, Sprite sprite, Body body) {
    float posX = body.getPosition().x;
    float posY = body.getPosition().y;
    sprite.setCenter(posX, posY);
    sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
    sprite.draw(batch);
  }
}

package dev.cgj.games.escape.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static dev.cgj.games.escape.Constants.SPRITE_TO_WORLD;

public class RenderUtils {

  /**
   * Draws a texture at a specified position in the world using sprite-to-world scaling.
   *
   * @param batch the SpriteBatch used for drawing to the screen
   * @param texture the Texture to be drawn
   * @param x the x-coordinate in sprite-space where the texture should be drawn
   * @param y the y-coordinate in sprite-space where the texture should be drawn
   */
  public static void drawTexture(SpriteBatch batch, Texture texture, int x, int y) {
    float worldX = x * SPRITE_TO_WORLD;
    float worldY = y * SPRITE_TO_WORLD;
    float worldWidth = texture.getWidth() * SPRITE_TO_WORLD;
    float worldHeight = texture.getHeight() * SPRITE_TO_WORLD;
    batch.draw(texture, worldX, worldY, worldWidth, worldHeight);
  }
}

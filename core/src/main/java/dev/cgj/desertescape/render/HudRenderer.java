package dev.cgj.desertescape.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import static dev.cgj.desertescape.Constants.SPRITE_TO_WORLD;
import static dev.cgj.desertescape.Constants.WORLD_HEIGHT;
import static dev.cgj.desertescape.Constants.WORLD_WIDTH;

public class HudRenderer implements Disposable {
  private final Camera camera;
  private final SpriteBatch spriteBatch;
  private final BitmapFont font;
  private final Texture hudOverlay;
  private final Texture arrow;

  public HudRenderer() {
    camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
    camera.update();
    spriteBatch = new SpriteBatch();
    hudOverlay = new Texture("sprites/ui/ui_overlay.png");
    arrow = new Texture("sprites/ui/arrow.png");
    font = new BitmapFont(Gdx.files.internal("fonts/kenney_mini.fnt"));
    font.setUseIntegerPositions(false);
    font.getData().setScale(SPRITE_TO_WORLD);
    font.setColor(Color.BLACK);
  }

  public void draw(HudData hudData) {
    spriteBatch.setProjectionMatrix(camera.combined);
    spriteBatch.begin();
    RenderUtils.drawTexture(spriteBatch, hudOverlay, 0, 0);
    RenderUtils.drawTexture(spriteBatch, arrow, 464, 150);
    spriteBatch.flush();
    drawHudData(hudData);
    spriteBatch.end();
  }

  private void drawHudData(HudData hudData) {
    drawHudString(String.valueOf(Math.round(hudData.speed())), 2, 163);
    drawHudString(String.valueOf(hudData.health()), 12, 142);
    drawHudString(String.valueOf(hudData.fuel()), 12, 132);
    drawHudString(String.valueOf(hudData.rockets()), 15, 115);
    drawHudString(String.valueOf(hudData.nitro()), 15, 104);
    drawHudString(String.valueOf(hudData.shield()), 15, 94);
    drawHudString(formatDistance(hudData.distance()), 458, 113);
    drawHudString(String.valueOf(hudData.score()), 458, 93);
  }

  /**
   * Draws a string on the HUD at the specified sprite-space coordinates, scaled to world-space.
   *
   * @param text the text to be drawn on the HUD
   * @param spriteX the x-coordinate in sprite-space where the text should be drawn
   * @param spriteY the y-coordinate in sprite-space where the text should be drawn
   */
  private void drawHudString(String text, float spriteX, float spriteY) {
    font.draw(spriteBatch, text, spriteX * SPRITE_TO_WORLD, spriteY * SPRITE_TO_WORLD);
  }

  /**
   * Formats a given distance value into a string representation with appropriate units (m or km).
   *
   * @param distance the distance to be formatted, expressed in meters
   * @return a string representation of the distance with the appropriate unit
   */
  private String formatDistance(int distance) {
    if (distance < 100) {
      return String.format("%dm", distance);
    }
    if (distance < 1000) {
      return String.format("%.1fkm", distance / 1000f);
    }
    return String.format("%dkm", distance / 1000);
  }

  @Override
  public void dispose() {
    hudOverlay.dispose();
  }
}

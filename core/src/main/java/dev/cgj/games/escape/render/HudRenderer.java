package dev.cgj.games.escape.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static dev.cgj.games.escape.Constants.SPRITE_TO_WORLD;

public class HudRenderer implements Disposable {
  private final Viewport hudViewport;
  private final SpriteBatch hudBatch;
  private final Texture hudOverlay;
  private final Texture arrow;
  private final BitmapFont font;

  public HudRenderer() {
    hudViewport = new FitViewport(480 * SPRITE_TO_WORLD, 270 * SPRITE_TO_WORLD);
    hudBatch = new SpriteBatch();
    hudOverlay = new Texture("sprites/ui/ui_overlay.png");
    arrow = new Texture("sprites/ui/arrow.png");
    font = new BitmapFont(Gdx.files.internal("fonts/kenney_mini.fnt"));
    font.setUseIntegerPositions(false);
    font.getData().setScale(SPRITE_TO_WORLD);
    font.setColor(Color.BLACK);
  }

  public void draw(HudData hudData) {
    // Update HUD viewport to match screen size
    hudViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    hudViewport.apply();
    hudBatch.setProjectionMatrix(hudViewport.getCamera().combined);
    hudBatch.begin();
    hudBatch.draw(hudOverlay, 0, 0, hudOverlay.getWidth() * SPRITE_TO_WORLD, hudOverlay.getHeight() * SPRITE_TO_WORLD);
    hudBatch.draw(arrow, 464 * SPRITE_TO_WORLD, 150 * SPRITE_TO_WORLD, arrow.getWidth() * SPRITE_TO_WORLD,
      arrow.getHeight() * SPRITE_TO_WORLD);
    hudBatch.flush();
    drawHudData(hudData);
    hudBatch.end();
  }

  private void drawHudData(HudData hudData) {
    drawHudString(String.format("%.1f", hudData.speed()), 2, 163);
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
    font.draw(hudBatch, text, spriteX * SPRITE_TO_WORLD, spriteY * SPRITE_TO_WORLD);
  }

  /**
   * Formats a given distance value into a string representation with appropriate units.
   * Distances less than 100 are displayed in meters (m). Distances between 100 and 1000 are
   * displayed in kilometers with one decimal place (km). Distances 1000 or greater are
   * displayed in whole kilometers (km).
   *
   * @param distance the distance to be formatted, expressed in meters
   * @return a string representation of the distance with the appropriate unit
   */
  private String formatDistance(int distance) {
    if (distance < 100) {
      return String.format("%dm", distance);
    }
    if (distance < 1000) {
      return String.format("%.1fkm", distance / 100f);
    }
    return String.format("%dkm", distance / 1000);
  }

  @Override
  public void dispose() {
    hudOverlay.dispose();
  }
}

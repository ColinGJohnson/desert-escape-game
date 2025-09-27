package dev.cgj.desertescape.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import static dev.cgj.desertescape.Constants.*;

public class HudRenderer implements Disposable {
  private static final int MIN_ARROW_POSITION = 126;
  private static final int MAX_ARROW_POSITION = 174;

  private final SpriteBatch spriteBatch;
  private final Camera camera;
  private final BitmapFont font;
  private final Texture hudOverlay;
  private final Texture arrow;

  public HudRenderer() {
    spriteBatch = new SpriteBatch();
    camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
    camera.update();

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
    RenderUtils.drawTexture(spriteBatch, arrow, 464, getArrowPosition(hudData));
    spriteBatch.flush();
    drawHudData(hudData);
    spriteBatch.end();
  }

  private int getArrowPosition(HudData hudData) {
    float progress = Math.clamp(hudData.distance() / GOAL_DISTANCE, 0, 1);
    return (int) (MIN_ARROW_POSITION + progress * (MAX_ARROW_POSITION - MIN_ARROW_POSITION));
  }

  private void drawHudData(HudData hudData) {
    drawHudString(String.valueOf(Math.round(hudData.speed())), 2, 163);
    drawHudString(String.valueOf(hudData.health()), 12, 142);
    drawHudString(String.valueOf((int) hudData.fuel()), 12, 132);
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
  private String formatDistance(float distance) {
    if (distance < 100) {
      return String.format("%dm", (int) distance);
    }
    if (distance < 1000) {
      return String.format("%.1fkm", distance / 1000f);
    }
    return String.format("%dkm", (int) distance / 1000);
  }

  @Override
  public void dispose() {
    hudOverlay.dispose();
  }
}

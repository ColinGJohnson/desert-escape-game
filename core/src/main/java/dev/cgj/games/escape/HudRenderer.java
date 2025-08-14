package dev.cgj.games.escape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HudRenderer implements Disposable {
  private final Viewport hudViewport;
  private final SpriteBatch hudBatch;
  private final Texture hudOverlay;

  public HudRenderer() {
    hudViewport = new FitViewport(480 * Constants.PIXEL_TO_WORLD, 270 * Constants.PIXEL_TO_WORLD);
    hudBatch = new SpriteBatch();
    hudOverlay = new Texture("sprites/ui/ui_overlay.png");
  }

  public void draw() {
    // Update HUD viewport to match screen size
    hudViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    hudViewport.apply();
    hudBatch.setProjectionMatrix(hudViewport.getCamera().combined);
    hudBatch.begin();
    hudBatch.draw(hudOverlay, 0, 0, hudViewport.getWorldWidth(), hudViewport.getWorldHeight());
    hudBatch.end();
  }

  @Override
  public void dispose() {
    hudOverlay.dispose();
  }
}

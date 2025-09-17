package dev.cgj.desertescape;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class DesertEscape extends Game {
  public FitViewport viewport;
  public SpriteBatch batch;

  /**
   * Box2D debug renderer
   */
  public Box2DDebugRenderer debugRenderer;

  /**
   * Low-resolution render target (480x270)
   */
  public FrameBuffer renderBuffer;

  /**
   * Low-res viewport used for rendering into the 480x270 framebuffer (48x27 world units)
   */
  public FitViewport lowResViewport;

  /**
   * Screen-space batch to draw {@code renderBuffer} scaled.
   */
  public SpriteBatch screenBatch;

  public void create() {
    batch = new SpriteBatch();
    screenBatch = new SpriteBatch();
    viewport = new FitViewport(480 * Constants.SPRITE_TO_WORLD, 270 * Constants.SPRITE_TO_WORLD);
    debugRenderer = new Box2DDebugRenderer();
    renderBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 480, 270, false);
    renderBuffer.getColorBufferTexture()
      .setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    this.setScreen(new GameScreen(this));
  }

  @Override
  public void render() {
    super.render();

    ScreenUtils.clear(Color.BLACK);
    screenBatch.begin();
    screenBatch.setColor(1, 1, 1, 1);

    // Calculate destination size using integer scaling with letterboxing.
    int windowW = Gdx.graphics.getWidth();
    int windowH = Gdx.graphics.getHeight();
    float scaleF = Math.min(windowW / 480f, windowH / 270f);
    int targetW = Math.max(1, Math.round(480 * scaleF));
    int targetH = Math.max(1, Math.round(270 * scaleF));
    int x = (windowW - targetW) / 2;
    int y = (windowH - targetH) / 2;

    // Draw with a negative height to flip the FBO texture vertically (FBOs are upside-down)
    screenBatch.draw(renderBuffer.getColorBufferTexture(), x, y + targetH, targetW, -targetH);
    screenBatch.end();
  }

  @Override
  public void resize(int width, int height) {
//    lowResViewport.update(480, 270, true);
  }

  public void dispose() {
    batch.dispose();
    screenBatch.dispose();
    if (renderBuffer != null) renderBuffer.dispose();
  }
}

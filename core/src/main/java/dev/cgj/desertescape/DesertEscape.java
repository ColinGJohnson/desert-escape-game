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

import static dev.cgj.desertescape.Constants.PIXEL_HEIGHT;
import static dev.cgj.desertescape.Constants.PIXEL_WIDTH;

public class DesertEscape extends Game {
  public FitViewport screenViewport;

  /**
   * Screen-space batch to draw {@code renderBuffer} scaled up to the {@code viewport} size.
   */
  public SpriteBatch screenBatch;

  public SpriteBatch renderBatch;

  /**
   * Low-resolution render target (480x270)
   */
  public FrameBuffer renderBuffer;

  /**
   * Box2D debug renderer
   */
  public Box2DDebugRenderer debugRenderer;

  public void create() {
    renderBatch = new SpriteBatch();
    screenBatch = new SpriteBatch();
    screenViewport = new FitViewport(PIXEL_WIDTH, PIXEL_HEIGHT);
    renderBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 480, 270, false);
    renderBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    debugRenderer = new Box2DDebugRenderer();
    this.setScreen(new MainMenuScreen(this));
  }

  @Override
  public void render() {
    super.render();
    ScreenUtils.clear(Color.BLACK);
    screenViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    screenBatch.setProjectionMatrix(screenViewport.getCamera().combined);
    screenBatch.begin();
    screenBatch.draw(renderBuffer.getColorBufferTexture(), 0, PIXEL_HEIGHT, PIXEL_WIDTH, -PIXEL_HEIGHT);
    screenBatch.end();
  }

  @Override
  public void resize(int width, int height) {
    screenViewport.update(width, height, true);
  }

  public void dispose() {
    renderBatch.dispose();
    screenBatch.dispose();
    if (renderBuffer != null) {
      renderBuffer.dispose();
    }
  }
}

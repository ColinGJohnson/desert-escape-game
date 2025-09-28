package dev.cgj.desertescape;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.cgj.desertescape.screens.GameScreen;
import dev.cgj.desertescape.screens.MainMenuScreen;

import static dev.cgj.desertescape.Constants.*;

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

  public boolean showDebug = false;

  public void create() {
    renderBatch = new SpriteBatch();
    screenBatch = new SpriteBatch();
    screenViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
    renderBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, PIXEL_WIDTH, PIXEL_HEIGHT, false);
    renderBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    debugRenderer = new Box2DDebugRenderer();
    this.setScreen(new MainMenuScreen(this));
  }

  @Override
  public void render() {
    super.render();
    handleInput();
    draw();
  }

  private void handleInput() {
    if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT_BRACKET)) {
      showDebug = !showDebug;
    }
  }

  private void draw() {
    ScreenUtils.clear(Color.BLACK);
    screenViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    screenBatch.setProjectionMatrix(screenViewport.getCamera().combined);
    screenBatch.begin();
    screenBatch.draw(renderBuffer.getColorBufferTexture(), 0, WORLD_HEIGHT, WORLD_WIDTH, -WORLD_HEIGHT);
    screenBatch.flush();

    if (showDebug && this.getScreen() instanceof GameScreen gameScreen) {
      debugRenderer.render(gameScreen.world, gameScreen.camera.combined);
    }

    screenBatch.end();
  }

  public void dispose() {
    renderBatch.dispose();
    screenBatch.dispose();
    if (renderBuffer != null) {
      renderBuffer.dispose();
    }
  }
}

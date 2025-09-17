package dev.cgj.desertescape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.cgj.desertescape.entity.Car;
import dev.cgj.desertescape.entity.CarType;
import dev.cgj.desertescape.physics.EntityContactListener;
import dev.cgj.desertescape.render.HudData;
import dev.cgj.desertescape.render.HudRenderer;
import dev.cgj.desertescape.terrain.TileManager;

public class GameScreen implements Screen {
  private final DesertEscape game;

  private float accumulator = 0;

  private final World world;

  private final Player player;

  private final TileManager tileManager;

  private final HudRenderer hudRenderer;

  private boolean showDebug = false;

  public GameScreen(final DesertEscape game) {
    this.game = game;
    world = new World(Vector2.Zero, true);
    world.setContactListener(new EntityContactListener());
    player = new Player(new Car(CarType.SPORTS, world), new Inventory());
    tileManager = new TileManager(world);
    hudRenderer = new HudRenderer();
    game.lowResViewport = new FitViewport(480 * Constants.SPRITE_TO_WORLD, 270 * Constants.SPRITE_TO_WORLD);
  }

  @Override
  public void render(float delta) {
    handleInput(delta);
    updateLogic(delta);
    stepPhysics(delta);
    draw();
  }

  @Override
  public void resize(int width, int height) {}

  @Override
  public void show() {}

  @Override
  public void hide() {}

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void dispose() {
    player.getCar().dispose();
    hudRenderer.dispose();
  }

  private void handleInput(float delta) {
    player.getCar().handleInput(delta, Gdx.input);

    if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
      game.setScreen(new MainMenuScreen(game));
      dispose();
    }

    if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT_BRACKET)) {
      showDebug = !showDebug;
    }
  }

  private void updateLogic(float delta) {
    player.update(delta);
    tileManager.update(player.getCar().body.carBody.getPosition());
  }

  /**
   * Fixed time step with max frame time to avoid a spiral of death on slow devices.
   *
   * @param delta Amount of time that has elapsed since the last frame, in milliseconds.
   */
  private void stepPhysics(float delta) {
    float frameTime = Math.min(delta, 0.25f);
    accumulator += frameTime;
    while (accumulator >= Constants.TIME_STEP) {
      world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
      accumulator -= Constants.TIME_STEP;
    }
  }

  private void draw() {
    updateCameraPosition();

    // Bind the low-res framebuffer
    game.renderBuffer.begin();
    ScreenUtils.clear(Color.BLACK);

    // Apply the low-res viewport that matches the 480x270 FBO
    // Do not center the camera here; we manage camera position manually in updateCameraPosition()
    game.lowResViewport.update(480, 270, false);
    game.lowResViewport.apply();

    // Scale the projection so that 1 world unit (0.1 sprite pixels) maps to 1 FBO pixel
    Matrix4 pixelProj = new Matrix4(game.lowResViewport.getCamera().combined).scale(0.5f, 0.5f, 1f);
    game.batch.setProjectionMatrix(pixelProj);
    game.batch.begin();
    tileManager.draw(game.batch);
    player.getCar().draw(game.batch);
    game.batch.end();

    // Render Box2D debug AFTER batch.end() into the same low-res target (below HUD)
    if (showDebug) {
      game.debugRenderer.render(world, pixelProj);
    }

    // Draw HUD into the same low-res framebuffer so it scales with the scene
    hudRenderer.drawWithProjection(pixelProj, getHudData(player));

    // Finish FBO pass
    game.renderBuffer.end();
  }

  private void updateCameraPosition() {
    Vector2 carPosition = player.getCar().body.carBody.getPosition().cpy();
    // TODO: Position camera based on car's velocity (requires smoothing)
    // carPosition.add(new Vector2(0, car.body.getForwardVelocity()).clamp(0, 10));
    // carPosition.add(new Vector2(0, 5).clamp(0, 10));
    // lowResViewport.getCamera().position.set(carPosition.cpy().add(new Vector2(24f, 23.5f)).scl(0.5f), 0);
    game.lowResViewport.getCamera().position.set(carPosition.cpy().add(new Vector2(24f, 23.5f)).scl(0.5f), 0);
    game.lowResViewport.getCamera().update();
  }

  private HudData getHudData(Player player) {
    return new HudData(CarType.SPORTS,
      player.getCar().body.getForwardVelocity(),
      player.getCar().getHealth(),
      (int) player.getCar().getFuel(),
      player.getInventory().getRockets(),
      player.getInventory().getNitro(),
      player.getInventory().getShield(),
      0f,
      (int) player.getCar().body.carBody.getPosition().y,
      9999);
  }
}

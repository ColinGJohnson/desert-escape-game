package dev.cgj.games.escape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.cgj.games.escape.entity.Car;
import dev.cgj.games.escape.terrain.TileManager;
import dev.cgj.games.old.CarType;

public class GameScreen implements Screen {
  private final DesertEscape game;
  private float accumulator = 0;
  private final World world;
  private final Car car;
  private final TileManager tileManager;
  private final HudRenderer hudRenderer;

  public GameScreen(final DesertEscape game) {
    this.game = game;
    world = new World(Vector2.Zero, true);
    car = new Car(CarType.SPORTS, world);
    tileManager = new TileManager(world);
    hudRenderer = new HudRenderer();
  }

  @Override
  public void render(float delta) {
    handleInput();
    updateLogic();
    stepPhysics(delta);
    draw();
  }

  @Override
  public void resize(int width, int height) {
    game.viewport.update(width, height, true);
  }

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
    car.dispose();
  }

  private void handleInput() {
    car.handleInput(Gdx.input);

    if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
      game.setScreen(new MainMenuScreen(game));
      dispose();
    }
  }

  private void updateLogic() {
    car.updatePhysics();
    tileManager.update(car.body.carBody.getPosition());
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
    game.viewport.getCamera().position.set(car.body.carBody.getPosition(), 0);
    ScreenUtils.clear(Color.BLACK);
    game.viewport.apply();
    game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
    game.batch.begin();

    tileManager.draw(game.batch);
    car.draw(game.batch);

    // Must flush batch before switching to font rendering
    game.batch.flush();
    game.font.draw(game.batch, "Test Text", 0, game.viewport.getWorldHeight());
    game.batch.end();

    // Render Box2D debug AFTER batch.end()
    game.debugRenderer.render(world, game.viewport.getCamera().combined);
    hudRenderer.draw();
  }
}

package dev.cgj.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.cgj.games.escape.entity.Car;
import dev.cgj.games.old.CarType;

public class GameScreen implements Screen {
  final DesertEscape game;
  private float accumulator = 0;

  Texture backgroundTexture;
  Car car;

  public GameScreen(final DesertEscape game) {
    this.game = game;
    backgroundTexture = new Texture("sprites/terrain/startGround2.png");
    car = new Car(CarType.SPORTS, game.world);
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    handleInput();
    updateLogic();
    stepPhysics(delta);
    draw();
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
      game.world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
      accumulator -= Constants.TIME_STEP;
    }
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
  }

  private void draw() {
    ScreenUtils.clear(Color.BLACK);
    game.viewport.apply();
    game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
    game.batch.begin();

    float worldWidth = game.viewport.getWorldWidth();
    float worldHeight = game.viewport.getWorldHeight();

    // Draw all sprites first (same texture binding)
    game.batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
    car.render(game.batch);

    // Flush batch before switching to font rendering
    game.batch.flush();

    game.font.draw(game.batch, "Test", 0, worldHeight);
    game.batch.end();

    // Render Box2D debug AFTER batch.end()
    game.debugRenderer.render(game.world, game.viewport.getCamera().combined);
  }

  @Override
  public void resize(int width, int height) {
    game.viewport.update(width, height, true);
  }

  @Override
  public void hide() {
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void dispose() {
    backgroundTexture.dispose();
    car.dispose();
  }
}

package dev.cgj.desertescape.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.cgj.desertescape.DesertEscape;
import dev.cgj.desertescape.Inventory;
import dev.cgj.desertescape.KeyMap;
import dev.cgj.desertescape.Player;
import dev.cgj.desertescape.ScoreBoard;
import dev.cgj.desertescape.entity.Car;
import dev.cgj.desertescape.entity.CarType;
import dev.cgj.desertescape.physics.EntityContactListener;
import dev.cgj.desertescape.render.HudData;
import dev.cgj.desertescape.render.HudRenderer;
import dev.cgj.desertescape.terrain.TileManager;

import static dev.cgj.desertescape.Constants.GOAL_DISTANCE;
import static dev.cgj.desertescape.Constants.POSITION_ITERATIONS;
import static dev.cgj.desertescape.Constants.TIME_STEP;
import static dev.cgj.desertescape.Constants.VELOCITY_ITERATIONS;
import static dev.cgj.desertescape.Constants.WORLD_HEIGHT;
import static dev.cgj.desertescape.Constants.WORLD_WIDTH;
import static dev.cgj.desertescape.KeyMap.KeyBinding.PAUSE;

public class GameScreen extends ScreenAdapter {
  private final DesertEscape game;
  public final Camera camera;
  public final World world;
  private final Player player;
  private final TileManager tileManager;
  private final HudRenderer hudRenderer;
  private float accumulator = 0;

  public GameScreen(final DesertEscape game) {
    this.game = game;
    camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    world = new World(Vector2.Zero, true);
    world.setContactListener(new EntityContactListener());
    player = new Player(new Car(CarType.SPORTS, world), new Inventory(), new ScoreBoard());
    tileManager = new TileManager(world);
    hudRenderer = new HudRenderer();
  }

  @Override
  public void render(float delta) {
    handleInput(delta);
    updateLogic(delta);
    stepPhysics(delta);
    draw();
  }

  @Override
  public void dispose() {
    player.car().dispose();
    hudRenderer.dispose();
  }

  private void handleInput(float delta) {
    player.handleInput(delta);

    if (KeyMap.isPressed(PAUSE)) {
      game.setScreen(new PauseScreen(game, this));
      dispose();
    }
  }

  private void updateLogic(float delta) {
    player.update(delta);
    tileManager.update(player.car().body.carBody.getPosition());
    if (player.car().body.carBody.getPosition().y > GOAL_DISTANCE) {
      game.setScreen(new WinScreen(game));
    }
  }

  /**
   * Fixed time step with max frame time to avoid a spiral of death on slow devices.
   *
   * @param delta Amount of time that has elapsed since the last frame, in milliseconds.
   */
  private void stepPhysics(float delta) {
    float frameTime = Math.min(delta, 0.25f);
    accumulator += frameTime;
    while (accumulator >= TIME_STEP) {
      world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
      accumulator -= TIME_STEP;
    }
  }

  private void draw() {
    updateCameraPosition();
    game.renderBuffer.begin();
    ScreenUtils.clear(Color.PURPLE);
    game.renderBatch.setProjectionMatrix(camera.combined);
    game.renderBatch.begin();
    tileManager.draw(game.renderBatch);
    player.car().draw(game.renderBatch);
    game.renderBatch.end();
    hudRenderer.draw(getHudData(player));
    game.renderBuffer.end();
  }

  private void updateCameraPosition() {
    Vector2 carPosition = player.car().body.carBody.getPosition().cpy();
    carPosition.add(new Vector2(0, 5).clamp(0, 10));
    camera.position.set(carPosition, 0);
    camera.update();
  }

  private HudData getHudData(Player player) {
    return new HudData(CarType.SPORTS,
      player.car().body.getForwardVelocity(),
      player.car().body.carBody.getPosition().y,
      player.car().getFuel(),
      player.car().getHealth(),
      player.inventory().getRockets(),
      player.inventory().getNitro(),
      player.inventory().getShield(),
      player.scoreBoard().getScore());
  }
}

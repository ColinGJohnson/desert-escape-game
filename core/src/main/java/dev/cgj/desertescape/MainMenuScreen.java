package dev.cgj.desertescape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;

public class MainMenuScreen implements Screen {
  private final DesertEscape game;
  public BitmapFont font;

  public MainMenuScreen(DesertEscape game) {
    this.game = game;
    font = new BitmapFont(Gdx.files.internal("fonts/kenney_mini.fnt"));
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(Color.BLACK);

    game.viewport.apply();
    game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

    game.batch.begin();
    font.draw(game.batch, "Desert Escape", 1, 1.5f);
    font.draw(game.batch, "Press any key to start", 1, 1);
    font.draw(game.batch, " !\"#$%&'()*+,-./0123456789:;" +
      "<=>?@ABCDEFGHIJKLMNOPQRSTU\nVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 0, 10);

    float worldHeight = game.viewport.getWorldHeight();
    font.draw(game.batch, "Desert Escape", 0, worldHeight);

    game.batch.end();

    if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
      System.out.println("Starting game...");
      game.setScreen(new GameScreen(game));
      dispose();
    }
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {
    font.dispose();
  }
}

package dev.cgj.desertescape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;

import java.util.List;

import static dev.cgj.desertescape.Constants.*;

public class MainMenuScreen extends ScreenAdapter {

  private enum MenuOptions {
    START("Start"),
    OPTIONS("Options"),
    EXIT( "Exit");

    private final String text;

    MenuOptions(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }
  }

  private final List<MenuOptions> menuOptions = List.of(MenuOptions.START, MenuOptions.OPTIONS, MenuOptions.EXIT);

  private final Camera camera;

  private final SpriteBatch spriteBatch;

  private final DesertEscape game;

  public BitmapFont font;

  private int selectionIndex = 0;

  public MainMenuScreen(DesertEscape game) {
    this.game = game;
    camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
    camera.update();
    spriteBatch = new SpriteBatch();
    font = new BitmapFont(Gdx.files.internal("fonts/kenney_mini.fnt"));
    font.setUseIntegerPositions(false);
    font.getData().setScale(SPRITE_TO_WORLD);
    font.setColor(Color.WHITE);
  }

  @Override
  public void render(float delta) {
    handleInput();
    draw();
  }

  @Override
  public void dispose() {
    font.dispose();
  }

  private void handleInput() {
    if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
      selectionIndex = (selectionIndex + menuOptions.size() - 1) % menuOptions.size();
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
      selectionIndex = (selectionIndex + 1) % menuOptions.size();
    }

    if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
      switch (menuOptions.get(selectionIndex)) {
        case START:
          game.setScreen(new GameScreen(game));
          break;
        case OPTIONS:
          break;
        case EXIT:
          Gdx.app.exit();
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + menuOptions.get(selectionIndex));
      }
    }
  }

  private void draw() {
    game.renderBuffer.begin();
    ScreenUtils.clear(Color.valueOf("#151D28"));

    spriteBatch.setProjectionMatrix(camera.combined);
    spriteBatch.begin();

    float lineHeight = 1.25f * font.getLineHeight();
    float leftMargin = 10;

    font.draw(spriteBatch, "DESERT ESCAPE", leftMargin, 20 + lineHeight);

    for (int i = 0; i < menuOptions.size(); i++) {
      MenuOptions option = menuOptions.get(i);
      boolean isSelected = i == selectionIndex;
      float y = 20 - lineHeight * i;
      String text = isSelected ? "> " + option.getText() : "  " + option.getText();
      font.draw(spriteBatch, text, leftMargin, y);
    }

    font.draw(spriteBatch, " !\"#$%&'()*+,-./0123456789:;" +
      "<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 0, 1);
    spriteBatch.end();
    game.renderBuffer.end();
  }
}

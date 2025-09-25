package dev.cgj.desertescape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.List;

import static dev.cgj.desertescape.Constants.*;

public class MainMenuScreen extends ScreenAdapter {

  Sound rolloverSound = Gdx.audio.newSound(Gdx.files.internal("audio/rollover1.ogg"));
  Sound clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/click1.ogg"));

  private enum MenuOptions {
    START("Start"),
    HELP("Controls"),
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

  private final List<MenuOptions> menuOptions = List.of(MenuOptions.START,
    MenuOptions.HELP,
    MenuOptions.OPTIONS,
    MenuOptions.EXIT
  );

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
  }

  @Override
  public void render(float delta) {
    handleInput();
    draw();
  }

  @Override
  public void dispose() {
    font.dispose();
    rolloverSound.dispose();
  }

  private void handleInput() {
    if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
      selectionIndex = (selectionIndex + menuOptions.size() - 1) % menuOptions.size();
      rolloverSound.play(1.0f);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
      selectionIndex = (selectionIndex + 1) % menuOptions.size();
      rolloverSound.play(1.0f);
    }

    if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
      clickSound.play(1.0f);
      switch (menuOptions.get(selectionIndex)) {
        case START:
          game.setScreen(new GameScreen(game));
          break;
        case HELP:
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

    final float leftMargin = 21f;
    final float bottomMargin = 16f;

    font.setColor(Color.valueOf("#BE772B"));
    font.draw(spriteBatch, "DESERT ESCAPE", leftMargin, bottomMargin + 1.5f * font.getLineHeight());

    for (int i = 0; i < menuOptions.size(); i++) {
      MenuOptions option = menuOptions.get(i);
      boolean isSelected = i == selectionIndex;
      float y = bottomMargin - 1.25f * font.getLineHeight() * i;
      String text = isSelected ? "> " + option.getText() : "  " + option.getText();
      font.setColor(isSelected ? Color.valueOf("#EBEDE9") : Color.valueOf("#C7CFCC"));
      font.draw(spriteBatch, text, leftMargin, y);
    }

    if (game.showDebug) {
      font.setColor(Color.LIME);
      font.draw(spriteBatch, " !\"#$%&'()*+,-./0123456789:;" +
        "<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 0, 1);
    }

    spriteBatch.end();
    game.renderBuffer.end();
  }
}

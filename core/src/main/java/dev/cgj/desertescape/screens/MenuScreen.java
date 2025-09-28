package dev.cgj.desertescape.screens;

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
import dev.cgj.desertescape.DesertEscape;

import java.util.List;

import static dev.cgj.desertescape.Constants.*;

public abstract class MenuScreen extends ScreenAdapter {
  public static final Color SELECTED_OPTION_COLOR = Color.valueOf("#EBEDE9");
  public static final Color OPTION_COLOR = Color.valueOf("#C7CFCC");
  public static final Color BACKGROUND_COLOR = Color.valueOf("#151D28");
  public static final Color TITLE_COLOR = Color.valueOf("#BE772B");

  private final Sound rolloverSound = Gdx.audio.newSound(Gdx.files.internal("audio/rollover1.ogg"));
  private final Sound clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/click1.ogg"));
  private final BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/kenney_mini.fnt"));
  private final Camera camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
  private final SpriteBatch spriteBatch = new SpriteBatch();
  private final DesertEscape game;
  private final List<MenuOption> options;
  private final String title;

  private int selectionIndex = 0;

  public MenuScreen(DesertEscape game, String title, List<MenuOption> options) {
    this.options = options;
    this.game = game;
    this.title = title;
    camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
    camera.update();
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
    if (upKeyPressed()) {
      rolloverSound.play(1.0f);
      selectionIndex = (selectionIndex + options.size() - 1) % options.size();
    } else if (downKeyPressed()) {
      rolloverSound.play(1.0f);
      selectionIndex = (selectionIndex + 1) % options.size();
    } else if (selectKeyPressed()) {
      clickSound.play(1.0f);
      options.get(selectionIndex).onClick();
    }
  }

  private void draw() {
    game.renderBuffer.begin();
    ScreenUtils.clear(BACKGROUND_COLOR);

    spriteBatch.setProjectionMatrix(camera.combined);
    spriteBatch.begin();

    final float leftMargin = 21f;
    final float bottomMargin = 16f;

    font.setColor(TITLE_COLOR);
    font.draw(spriteBatch, title, leftMargin, bottomMargin + 1.5f * font.getLineHeight());

    for (int i = 0; i < options.size(); i++) {
      MenuOption option = options.get(i);
      boolean isSelected = i == selectionIndex;
      float y = bottomMargin - 1.25f * font.getLineHeight() * i;
      String template = isSelected ? "> %s" : "  %s";
      font.setColor(isSelected ? SELECTED_OPTION_COLOR : OPTION_COLOR);
      font.draw(spriteBatch, template.formatted(option.getText()), leftMargin, y);
    }

    if (game.showDebug) {
      font.setColor(Color.LIME);
      font.draw(spriteBatch, " !\"#$%&'()*+,-./0123456789:;" +
        "<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 0, 1);
    }

    spriteBatch.end();
    game.renderBuffer.end();
  }


  private static boolean downKeyPressed() {
    return Gdx.input.isKeyJustPressed(Input.Keys.DOWN)
      || Gdx.input.isKeyJustPressed(Input.Keys.S);
  }

  private static boolean upKeyPressed() {
    return Gdx.input.isKeyJustPressed(Input.Keys.UP)
      || Gdx.input.isKeyJustPressed(Input.Keys.W);
  }

  private static boolean selectKeyPressed() {
    return Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
      Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
  }
}

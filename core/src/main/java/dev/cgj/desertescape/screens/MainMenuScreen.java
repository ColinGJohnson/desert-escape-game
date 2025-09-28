package dev.cgj.desertescape.screens;

import com.badlogic.gdx.Gdx;
import dev.cgj.desertescape.DesertEscape;

import java.util.List;

public class MainMenuScreen extends MenuScreen {
  public MainMenuScreen(DesertEscape game) {
    super(game, "DESERT ESCAPE", List.of(
      new MenuOption("Start", () -> game.setScreen(new GameScreen(game))),
      new MenuOption("Controls", () -> {
      }),
      new MenuOption("Options", () -> {
      }),
      new MenuOption("Exit", () -> Gdx.app.exit())));
  }
}

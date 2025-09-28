package dev.cgj.desertescape.screens;

import com.badlogic.gdx.Gdx;
import dev.cgj.desertescape.DesertEscape;

import java.util.List;

public class WinScreen extends MenuScreen {
  public WinScreen(DesertEscape game) {
    super(game, "YOU ESCAPED!", List.of(
      new MenuOption("Main Menu", () -> game.setScreen(new MainMenuScreen(game))),
      new MenuOption("Exit", () -> Gdx.app.exit())));
  }
}

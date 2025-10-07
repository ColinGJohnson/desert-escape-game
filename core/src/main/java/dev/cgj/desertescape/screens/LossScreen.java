package dev.cgj.desertescape.screens;

import dev.cgj.desertescape.DesertEscape;

import java.util.List;

public class LossScreen extends MenuScreen {
  public LossScreen(DesertEscape game) {
    super(game, "GAME OVER", List.of(
      new MenuOption("Main Menu", () -> game.setScreen(new MainMenuScreen(game))),
      new MenuOption("Restart", () -> game.setScreen(new GameScreen(game)))
    ));
  }
}

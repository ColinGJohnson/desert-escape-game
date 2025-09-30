package dev.cgj.desertescape.screens;

import dev.cgj.desertescape.DesertEscape;

import java.util.List;

public class PauseScreen extends MenuScreen {
  public PauseScreen(DesertEscape game, GameScreen gameScreen) {
    super(game, "PAUSE", List.of(
      new MenuOption("Resume", () -> game.setScreen(gameScreen)),
      new MenuOption("Restart", () -> game.setScreen(new GameScreen(game))),
      new MenuOption("Main Menu", () -> game.setScreen(new MainMenuScreen(game)))
    ));
  }
}

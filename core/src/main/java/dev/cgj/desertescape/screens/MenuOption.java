package dev.cgj.desertescape.screens;

public class MenuOption {
  private final String text;
  private final Runnable onClick;

  public MenuOption(String text, Runnable onClick) {
    this.text = text;
    this.onClick = onClick;
  }

  public String getText() {
    return text;
  }

  public void onClick() {
    onClick.run();
  }
}

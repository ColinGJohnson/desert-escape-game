package dev.cgj.desertescape.entity;

import dev.cgj.desertescape.Player;

import java.util.function.Consumer;

public enum PowerupType {
  FUEL("sprites/powerups/fuel.png", player -> player.car().refuel(10)),
  HEALTH("sprites/powerups/health.png", player -> player.car().repair(1)),
  NITRO("sprites/powerups/nitro.png", player -> player.inventory().addNitro(1)),
  ROCKET("sprites/powerups/rocket.png", player -> player.inventory().addRockets(1)),
  SHIELD("sprites/powerups/shield.png", player -> player.inventory().addShields(1));

  private final String spritePath;
  private final Consumer<Player> onCollect;

  PowerupType(String spritePath, Consumer<Player> onCollect) {
    this.spritePath = spritePath;
    this.onCollect = onCollect;
  }

  public void onCollect(Player player) {
    onCollect.accept(player);
  }

  public String getSpritePath() {
    return spritePath;
  }
}

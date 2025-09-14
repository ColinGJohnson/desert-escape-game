package dev.cgj.desertescape.entity;

public enum ObstacleType {
  CACTUS("sprites/obstacles/cactus.png"),
  CONE("sprites/obstacles/cone.png"),
  SKULL("sprites/obstacles/skull.png");

  private final String spritePath;

  ObstacleType(String spritePath) {
    this.spritePath = spritePath;
  }

  public String getSpritePath() {
    return spritePath;
  }
}

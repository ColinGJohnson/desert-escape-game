package dev.cgj.desertescape.entity;

public enum ObstacleType {
  CACTUS("sprites/obstacles/cactus.png", 1),
  CONE("sprites/obstacles/cone.png", 1),
  SKULL("sprites/obstacles/skull.png", 1);

  private final String spritePath;
  private final int damage;

  ObstacleType(String spritePath, int damage) {
    this.spritePath = spritePath;
    this.damage = damage;
  }

  public String getSpritePath() {
    return spritePath;
  }

  public int getDamage() {
    return damage;
  }
}

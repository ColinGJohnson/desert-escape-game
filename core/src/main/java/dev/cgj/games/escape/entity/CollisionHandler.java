package dev.cgj.games.escape.entity;

public interface CollisionHandler {
  default void handleCollision(CollisionHandler other) { }
}

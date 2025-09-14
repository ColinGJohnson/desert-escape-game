package dev.cgj.desertescape.entity;

public interface CollisionHandler {
  default void handleCollision(CollisionHandler other) { }
}

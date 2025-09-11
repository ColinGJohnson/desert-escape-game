package dev.cgj.games.escape.physics;

import java.util.function.Consumer;

public class UserData {

  private final Consumer<Object> collisionHandler;

  private final Object collisionData;

  public UserData(Consumer<Object> collisionHandler, Object collisionData) {
    this.collisionHandler = collisionHandler;
    this.collisionData = collisionData;
  }

  public UserData(Consumer<Object> collisionHandler) {
    this(collisionHandler, null);
  }

  public void handleCollision(Object collisionData) {
    collisionHandler.accept(collisionData);
  }

  public Object getCollisionData() {
    return collisionData;
  }
}

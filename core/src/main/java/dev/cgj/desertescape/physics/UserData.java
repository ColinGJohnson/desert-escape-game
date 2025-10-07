package dev.cgj.desertescape.physics;

import java.util.function.Consumer;

public class UserData {

  private final Consumer<Object> collisionHandler;

  private final Object collisionData;

  public UserData(Consumer<Object> collisionHandler, Object collisionData) {
    this.collisionHandler = collisionHandler;
    this.collisionData = collisionData;
  }

  public static UserData dataOnly(Object collisionData) {
    return new UserData((ignored) -> {
    }, collisionData);
  }

  public static UserData handlerOnly(Consumer<Object> collisionHandler) {
    return new UserData(collisionHandler, null);
  }

  public void handleCollision(Object collisionData) {
    collisionHandler.accept(collisionData);
  }

  public Object getCollisionData() {
    return collisionData;
  }
}

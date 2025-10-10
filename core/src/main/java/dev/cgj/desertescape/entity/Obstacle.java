package dev.cgj.desertescape.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.desertescape.Player;
import dev.cgj.desertescape.physics.UserData;

public class Obstacle extends Entity {
  private final ObstacleType type;

  public Obstacle(ObstacleType type, World world, Vector2 position) {
    super(type.getSpritePath(), world, position);
    this.type = type;
    getBody().setUserData(UserData.handlerOnly(this::handleCollision));
  }

  public void handleCollision(Object entity) {
    if (isDestroyed()) {
      return;
    }

    switch (entity) {
      case Player player -> {
        player.car().damage(type.getDamage());
        setDestroyed(true);
      }
      case null, default -> {}
    }
  }
}

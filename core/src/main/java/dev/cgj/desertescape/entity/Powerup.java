package dev.cgj.desertescape.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.desertescape.Player;
import dev.cgj.desertescape.physics.UserData;

public class Powerup extends Entity {
  private final PowerupType type;
  private boolean collided;

  public Powerup(PowerupType type, World world, Vector2 position) {
    super(type.getSpritePath(), world, position);
    this.type = type;
    getBody().setUserData(UserData.handlerOnly(this::handleCollision));
  }

  void handleCollision(Object entity) {
    if (collided) {
      return;
    }

    switch (entity) {
      case Player player -> {
        type.onCollect(player);
        collided = true;
      }
      case null, default -> {
      }
    }
  }
}

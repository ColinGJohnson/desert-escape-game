package dev.cgj.desertescape.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.desertescape.Player;
import dev.cgj.desertescape.physics.UserData;

import static dev.cgj.desertescape.physics.BodyUtils.createStaticBody;

public class Powerup extends Entity {
  public static final Vector2 SIZE = new Vector2(16f, 16f);
  private final PowerupType type;

  public Powerup(PowerupType type, World world, Vector2 position) {
    super(type.getSpritePath(), createStaticBody(world, position, SIZE));
    this.type = type;
    getBody().setUserData(UserData.handlerOnly(this::handleCollision));
  }

  void handleCollision(Object entity) {
    if (isDestroyed()) {
      return;
    }

    switch (entity) {
      case Player player -> {
        type.onCollect(player);
        setDestroyed(true);
      }
      case null, default -> {
      }
    }
  }
}

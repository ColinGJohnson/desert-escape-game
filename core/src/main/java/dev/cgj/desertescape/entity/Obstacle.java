package dev.cgj.desertescape.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.desertescape.Player;
import dev.cgj.desertescape.physics.UserData;

import static dev.cgj.desertescape.physics.BodyUtils.createStaticBody;

public class Obstacle extends Entity {
  private static final Vector2 SIZE = new Vector2(8f, 8f);
  private final ObstacleType type;

  public Obstacle(ObstacleType type, World world, Vector2 position) {
    super(type.getSpritePath(), createStaticBody(world, position, SIZE));
    this.type = type;
    getBody().setUserData(UserData.handlerOnly(this::handleCollision));
  }

  public void handleCollision(Object entity) {
    if (isDestroyed()) {
      return;
    }

    if (entity instanceof Player player) {
      player.car().damage(type.getDamage());
    }

    setDestroyed(true);
  }
}

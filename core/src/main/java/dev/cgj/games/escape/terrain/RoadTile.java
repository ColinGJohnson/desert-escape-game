package dev.cgj.games.escape.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.games.escape.entity.Obstacle;
import dev.cgj.games.escape.entity.ObstacleType;

import java.util.List;

public class RoadTile implements TileDefinition {

  @Override
  public Texture getTexturePath() {
    return new Texture("sprites/terrain/ground.png");
  }

  @Override
  public List<Body> addStaticBodies(World world) {
    return List.of();
  }

  @Override
  public List<Obstacle> addObstacles(World world) {
    // TODO: Randomly position obstacles on tile in zones
    return List.of(
      new Obstacle(ObstacleType.CACTUS, world, new Vector2(5, 10)),
      new Obstacle(ObstacleType.SKULL, world, new Vector2(5, 15)),
      new Obstacle(ObstacleType.CONE, world, new Vector2(5, 20))
    );
  }
}

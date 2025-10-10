package dev.cgj.desertescape.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.desertescape.entity.Entity;
import dev.cgj.desertescape.entity.Obstacle;
import dev.cgj.desertescape.entity.ObstacleType;
import dev.cgj.desertescape.entity.Powerup;
import dev.cgj.desertescape.entity.PowerupType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.cgj.desertescape.Constants.spriteToWorld;

public class RoadTile implements TileDefinition {
  private static final int POWERUPS_PER_TILE = 2;

  SpawnZone leftDirt = new SpawnZone(spriteToWorld(0, 0), spriteToWorld(90, 270));
  SpawnZone rightDirt = new SpawnZone(spriteToWorld(180, 0), spriteToWorld(270, 270));
  SpawnZone road = new SpawnZone(spriteToWorld(120, 0), spriteToWorld(150, 270));

  @Override
  public Texture getTexturePath() {
    return new Texture("sprites/terrain/ground.png");
  }

  @Override
  public List<Body> addStaticBodies(World world) {
    return List.of();
  }

  @Override
  public List<Entity> addEntities(World world) {
    List<Entity> entities = new ArrayList<>(addObstacles(world));
    entities.addAll(addPowerups(world));
    return entities;
  }

  public List<Obstacle> addObstacles(World world) {
    Stream<Obstacle> left = Stream.generate(() ->
        new Obstacle(ObstacleType.SKULL, world, leftDirt.randomPosition()))
      .limit(5);
    Stream<Obstacle> right = Stream.generate(() ->
        new Obstacle(ObstacleType.CACTUS, world, rightDirt.randomPosition()))
      .limit(5);
    Stream<Obstacle> cones = Stream.generate(() ->
        new Obstacle(ObstacleType.CONE, world, road.randomPosition()))
      .limit(1);
    return Stream.concat(Stream.concat(left, right), cones).collect(Collectors.toList());
  }

  public List<Powerup> addPowerups(World world) {
    PowerupType[] types = PowerupType.values();
    List<Powerup> powerups = new ArrayList<>(POWERUPS_PER_TILE);
    for (int i = 0; i < POWERUPS_PER_TILE; i++) {
      PowerupType randomType = types[ThreadLocalRandom.current().nextInt(types.length)];
      powerups.add(new Powerup(randomType, world, road.randomPosition()));
    }
    return powerups;
  }
}

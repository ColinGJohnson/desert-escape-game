package dev.cgj.desertescape.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import dev.cgj.desertescape.entity.Entity;

import java.util.List;

public interface TileDefinition {

  Texture getTexturePath();

  List<Body> addStaticBodies(World world);

  default List<Entity> addEntities(World world) {
    return List.of();
  }
}

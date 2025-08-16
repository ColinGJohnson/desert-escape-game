package dev.cgj.games.escape.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

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
}
